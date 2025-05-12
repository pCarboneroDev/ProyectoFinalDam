package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.models.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TunerVM @Inject constructor(
    private val notesRepo: MusicNoteRepository,
    private val tuningRepo: TuningRepository,
    private val tuningMusicNoteRepo: TuningMusicNoteRepository,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    private val sampleRate = 44100
    private val bufferSize = 16384
    private lateinit var audioRecord: AudioRecord


    private val _isRecording = MutableStateFlow<Boolean>(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _freqFound = MutableStateFlow<Double>(0.0)
    val freqFound: StateFlow<Double> = _freqFound

    private val _noteList = MutableStateFlow<List<MusicNote>>(emptyList())
    val noteList: StateFlow<List<MusicNote>> = _noteList

    private val _selectedNote = MutableStateFlow<MusicNote?>(null)
    val selectedNote: StateFlow<MusicNote?> = _selectedNote

    private  val _guideText = MutableStateFlow<String>("")
    val guideText: StateFlow<String> = _guideText

    private val _tunings = MutableStateFlow<List<TuningWithNotesModel>>(emptyList())
    val tunings: StateFlow<List<TuningWithNotesModel>> = _tunings

    private val _selectedTuning = MutableStateFlow<TuningWithNotesModel?>(null)
    val selectedTuning: StateFlow<TuningWithNotesModel?> = _selectedTuning

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes


    //SETTERS

    fun setIsRecording(value: Boolean){
        _isRecording.value = value
    }

    fun setSelectedNote(value: MusicNote){
        _selectedNote.value = value
    }

    fun setSelectedTuning(value: TuningWithNotesModel){
        _selectedTuning.value = value
    }


    // ALGO ASI COMO EL CONSTRUCTOR
    init {
        var list: List<MusicNote>
        viewModelScope.launch(Dispatchers.IO) {
            list = notesRepo.getAllNotes()
            _noteList.value = list

            // obtenemos las tunings hasta el momento
            var tunings = tuningRepo.getAllTunings()
            val tuningList = mutableListOf<TuningWithNotesModel>()

            for (tuning in tunings){ //accesoBD.obtenerIdNotasPorIdAfinacion(tuning.id)
                var notesIdList = tuningMusicNoteRepo.getNotesIdFromTuningId(tuning.id)
                var noteList = mutableListOf<MusicNote>()

                for(noteId in notesIdList ){ //accesoBD.obtenerNotaPorId(noteId)
                    var note = notesRepo.getMusicNoteById(noteId)
                    noteList.add(note)
                }

                noteList.sort()

                var tuningToInsert = TuningWithNotesModel(tuning = tuning, noteList = noteList)
                tuningList.add(tuningToInsert)
            }
            _tunings.value = tuningList
            _latinNotes.value = preferencesRepo.getNotationPreference()
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    /**
    * Metodo que se encarga de inicializar el audiorecord con las propiedades necesarias
    */
    fun initializeAudioRecord() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, sampleRate,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize
        )
    }

    /**
     * Metodo que se encarga de capturar el audio y pasarlo a otros metodos que se encargan de realizar el análisis del audio
     */
    fun startRecordingAudio(){
        viewModelScope.launch(Dispatchers.IO) {
            audioRecord.startRecording()
            while (_isRecording.value == true){
                var txt = ""
                val freq = processYIN(audioRecord, bufferSize, sampleRate)
                _freqFound.value = freq

                if(selectedNote.value != null && _freqFound.value != null){
                    if (_freqFound.value!! > _selectedNote.value!!.minHz && _freqFound.value!! < _selectedNote.value!!.maxHz){
                        txt = "Note in tune"
                    }
                    else if( _freqFound.value!! > _selectedNote.value!!.maxHz ){
                        txt = "Loosen the string"
                    }
                    else if( _freqFound.value!! < _selectedNote.value!!.minHz ){
                        txt = "Tighten the string"
                    }
                    _guideText.value = txt
                }

            }
            audioRecord.stop()
            _guideText.value = ""
        }
    }

    /**
     * Este es el metodo principal para identificar una nota musical con las señales digitales de audio
     * Utiliza un algoritmo conocido como YIN que se especializa en la identificación de notas
     * Es mejor que otros metodos cómo FFT ya que YIN sí es capaz de ignorar armónicos.
     */
    fun processYIN(audioRecord: AudioRecord, bufferSize: Int, sampleRate: Int): Double {
        val buffer = ShortArray(bufferSize)
        val numOfSamples = audioRecord.read(buffer, 0, bufferSize)
        if (numOfSamples <= 0) return 0.0

        val signal = DoubleArray(numOfSamples) { buffer[it].toDouble() }
        val tauMin = sampleRate / 2000  // Frecuencia máxima detectable
        val tauMax = sampleRate / 20    // Frecuencia mínima detectable

        val differenceFunction = DoubleArray(tauMax)
        val cumulativeMeanNormalizedDifferenceFunction = DoubleArray(tauMax)


        // se mide la diferencia entre la señal y su versión mas avanzada en el tiempo
        // cuanto menor la diferencia mas probable de que haya una frecuencia fundamental
        // de esta forma se pueden ignorar los armónicos de las notas
        for (tau in tauMin until tauMax) {
            var sum = 0.0
            for (i in 0 until (numOfSamples - tau)) {
                val diff = signal[i] - signal[i + tau]
                sum += diff * diff
            }
            differenceFunction[tau] = sum
        }

        // se normaliza para que sea mas preciso
        cumulativeMeanNormalizedDifferenceFunction[0] = 1.0
        for (tau in 1 until tauMax) {
            var sum = 0.0
            for (j in 1..tau) {
                sum += differenceFunction[j]
            }
            cumulativeMeanNormalizedDifferenceFunction[tau] =
                if (sum == 0.0) 1.0 else differenceFunction[tau] / (sum / tau)
        }

        // se busca el primer valle por debajo del umbral
        // si el valle es menor 0.1 es la frecuencia fundamental,
        // por lo que es la frecuencia de la nota principal en el momento
        val threshold = 0.1
        var tauEstimate = -1
        var aux = 0
        for (tau in tauMin until tauMax) {
            aux = tau
            if (cumulativeMeanNormalizedDifferenceFunction[tau] < threshold) {
                while (aux + 1 < tauMax && cumulativeMeanNormalizedDifferenceFunction[aux + 1] < cumulativeMeanNormalizedDifferenceFunction[aux]) {
                    aux++
                }
                tauEstimate = aux
                break
            }
        }
        // se sigue la fórmula f = samplerate/tau
        //return if (tauEstimate != -1) sampleRate.toDouble() / tauEstimate else 0.0
        var f = 0.0

        if (tauEstimate != -1){
            f = sampleRate.toDouble()/tauEstimate
        }

        return f
    }


    // PREFERENCIAS
    /*fun obtenerPreferencias(){
        // ver preferencias usuario
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepo.getNotationPreference()
        }

    }*/
}