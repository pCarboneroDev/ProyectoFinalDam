package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAllTuningWithNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAmountTuningsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.pow

@HiltViewModel
class TunerVM @Inject constructor(
    private val getAllTuningWithNotesUseCase: GetAllTuningWithNotesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    private val startingTuning: String? = savedStateHandle["selectedTuningId"]

    private val _tunings = MutableStateFlow<List<TuningWithNotesModel>>(emptyList())
    val tunings: StateFlow<List<TuningWithNotesModel>> = _tunings

    private val _selectedTuning = MutableStateFlow<TuningWithNotesModel?>(null)
    val selectedTuning: StateFlow<TuningWithNotesModel?> = _selectedTuning

    private val _selectedNote = MutableStateFlow<MusicNote?>(null)
    val selectedNote: StateFlow<MusicNote?> = _selectedNote

    private val _isRecording = MutableStateFlow<Boolean>(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _freqFound = MutableStateFlow<Double>(0.0)
    val freqFound: StateFlow<Double> = _freqFound

    private  val _guideText = MutableStateFlow<String>("")
    val guideText: StateFlow<String> = _guideText

    private val sampleRate = 44100
    private val bufferSize = 16384
    private lateinit var audioRecord: AudioRecord

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes

    private val _graphValue = MutableStateFlow<Double>(0.5)
    val graphValue: StateFlow<Double> = _graphValue

    private val _colorGraph = MutableStateFlow<Color>(Color(0xFF18120C))
    val colorGraph: StateFlow<Color> = _colorGraph



    //SETTERS

    fun setIsRecording(value: Boolean){
        _isRecording.value = value
    }

    fun setSelectedNote(value: MusicNote?){
        _selectedNote.value = value
    }

    fun setSelectedTuning(value: TuningWithNotesModel){
        _selectedTuning.value = value
    }


    // ALGO ASI COMO EL CONSTRUCTOR
    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadTunings()
            loadPreferences()

            try {
                if(!startingTuning.isNullOrEmpty()){
                    _selectedTuning.value = _tunings.value.find { (x) -> x.id == startingTuning.toLong() }
                }
            }catch (e: Exception){
                //todo gestionar :(
            }
        }
    }

    /**
     * Llama al caso de uso que le devuelve una lista de TuningWithNotes
     */
    suspend fun loadTunings(){
        _tunings.value = getAllTuningWithNotesUseCase.call(Unit)
    }

    /**
     * Comprueba la configuración de usuario para mostrar las notas
     */
    suspend fun loadPreferences(){
        _latinNotes.value = preferencesRepo.getNotationPreference()
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
     *  f(x)=1/(1+e−k(x−objetivo))
     */
    fun startRecordingAudio(){
        viewModelScope.launch(Dispatchers.IO) {
            audioRecord.startRecording()
            while (_isRecording.value == true){
                var txt = ""
                val freq = processYIN(audioRecord, bufferSize, sampleRate)
                _freqFound.value = freq

               // 1.0 / (1.0 + exp(-0.01 * (freq - center)))

                if(selectedNote.value != null /*&& _freqFound.value != 0.0*/){
                    _graphValue.value = sigmoidNormalizedBetween(freq,
                        selectedNote.value!!.minHz, _selectedNote.value!!.maxHz)

                    if (_freqFound.value == 0.0){
                        txt = ""
                        _colorGraph.value = Color(0xFF18120C)
                    }
                    else if (_freqFound.value >= _selectedNote.value!!.minHz && _freqFound.value <= _selectedNote.value!!.maxHz){
                        txt = "Note in tune"
                        _colorGraph.value = Color.Green
                    }
                    else if( _freqFound.value > _selectedNote.value!!.maxHz ){
                        txt = "Loosen the string"
                        _colorGraph.value = Color.Red
                    }
                    else if( _freqFound.value < _selectedNote.value!!.minHz ){
                        txt = "Tighten the string"
                        _colorGraph.value = Color.Red
                    }
                    _guideText.value = txt
                }

            }
            audioRecord.stop()
            _guideText.value = ""
            _graphValue.value = 0.5
            _colorGraph.value = Color(0xFF18120C)
        }
    }

    /**
     * Metodo que se encarga de calcular el punto medio de el rango de frecuencias
     * y da un valor a la frecuencia encontrada entre 0 y 1 para ubicar el círculo que muestra
     * la afinación de la nota seleccionada
     */
    fun sigmoidNormalizedBetween(freq: Double, minHz: Double, maxHz: Double, kFactor: Double = 0.02): Double {
        if (freq == 0.0) return 0.5

        val center = (minHz + maxHz) / 2.0
        val rangeWidth = maxHz - minHz
        val k = kFactor / rangeWidth
        return 1.0 / (1.0 + exp(-k * (freq - center)))
    }

    /**
     * Este es el metodo principal para identificar una nota musical con las señales digitales de audio
     * Utiliza un algoritmo conocido como YIN que se especializa en la identificación de notas, ya que identifica la frecuencia fundamental
     * Es mejor que otros metodos cómo FFT ya que YIN sí es capaz de ignorar armónicos.
     *
     * @param audioRecord: instancia de AudioRecord que captura el audio
     * @param bufferSize: cantidad de muestras que se guardan antes de analizarlas
     * @param sampleRate: cantidad de muestras de audio que se toman por segundo
     * Return: valor de la frecuencia detectada, 0 si no detecta nada.
     */
    fun processYIN(audioRecord: AudioRecord, bufferSize: Int, sampleRate: Int): Double {
        // se crea un array de shorts del tamaño del número de muestras que se pueden capturar
        val buffer = ShortArray(bufferSize)
        // se comprueba si audio record tiene muestras capturadas, si no devuelkve 0
        val numOfSamples = audioRecord.read(buffer, 0, bufferSize)
        if (numOfSamples <= 0) return 0.0

        //  convierte los valores enteros del audio a Double, que se necesita para mayor precision en los cálculos
        val signal = DoubleArray(numOfSamples) { buffer[it].toDouble() }
        // se configuran la frecuencia máxima y mínima detectables
        val tauMin = sampleRate / 2000
        val tauMax = sampleRate / 20

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
        var f = 0.0

        if (tauEstimate != -1){
            f = sampleRate.toDouble()/tauEstimate
        }

        return f
    }
}