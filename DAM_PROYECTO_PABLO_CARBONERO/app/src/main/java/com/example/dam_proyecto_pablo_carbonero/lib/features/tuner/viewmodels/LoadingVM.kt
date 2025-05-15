package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class LoadingVM @Inject constructor (
    private val notesRepo: MusicNoteRepository,
    private val tuningRepo: TuningRepository,
    private val tuningMusicNoteRepo: TuningMusicNoteRepository
): ViewModel() {

    private val _loadComplete = MutableLiveData(false)
    val loadComplete: LiveData<Boolean> = _loadComplete


    private val _txtPlaceholder = MutableLiveData<String>()
    val txtPlaceholder: LiveData<String> = _txtPlaceholder

    // esto se ejecuta al instanciar el viewModel
    init {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                var notes = notesRepo.getAllNotes()
                val tunings = tuningRepo.getAllTunings()

                if (notes.isEmpty()) {
                    notes = generateMusicNotes()
                }
                if(tunings.isEmpty()){
                    generateStandardTuning(notes)
                }
                _loadComplete.postValue(true)
            }catch (e: Exception){
                //TODO gestionar excepción
            }
        }
    }

    /**
     * Metodo que se encarga de generar todas las notas musicales y de añadirlas a la base de datos local
     */
    private suspend fun generateMusicNotes(): List<MusicNote>{
        val notesListToInsert = getFreqAccordingOnA4();

        notesRepo.insertAllMusicNotes(notesListToInsert)
        return notesRepo.getAllNotes()
    }

    private suspend fun generateStandardTuning(notes: List<MusicNote>){
        val list = mutableListOf<MusicNote>()

        // Notas estándar para una guitarra de 6 cuerdas
        val standardTuning = listOf("E2", "A2", "D3", "G3", "B3", "E4")

        for (TuningNote in standardTuning) {
            val noteFound = notes.find { it.englishName == TuningNote }
            if (noteFound != null) {
                list.add(noteFound)
            } else {
                println("Nota $TuningNote no encontrada en la lista de notas")
            }
        }

        var standardTunning = Tuning(name = "Standard Tuning");

        var standardId = tuningRepo.insertTuning(standardTunning)

        for (nota in list){
            var insert = TuningMusicNote(tuningId = standardId, noteId = nota.id)
            tuningMusicNoteRepo.insertTuningMusicNote(insert)
        }
    }


    // Metodos de insercion de datos
    fun getFreqAccordingOnA4(): List<MusicNote> {
        var freqRef: Double = 440.0
        var index = 0;

        var noteList = listOf<String>("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#","A", "A#", "B")


        var englishNames = listOf<String>("A", "B", "C", "D", "E", "F", "G")
        var LatinNames = listOf<String>("La", "Si", "Do", "Re", "Mi", "Fa", "Sol")

        var musicNoteList = mutableListOf<MusicNote>();


        for (note in noteList){
            for (i in 1..4){
                var octave = i;

                var semitone = calculateSemitone(octave, noteList.indexOf(note))
                var freqWanted = calculateFreq(freqRef, semitone);

                var indexN = englishNames.indexOf(note[0].toString())
                var sost: String?;
                var bem: String?;

                if(note.contains('#')){
                    sost = "#"
                    bem = "b"
                }
                else{
                    sost = null
                    bem = null
                }


                var nota = MusicNote(latinNotation = LatinNames[indexN], englishNotation = englishNames[indexN],
                    octave = octave, maxHz = freqWanted+0.5, minHz = freqWanted-0.5,
                    sharpIndicator = sost, alternativeName = bem)



                musicNoteList.add(nota);
            }
            index++
        }
        return musicNoteList;
    }

    /**
     * Metodo que calcula la frecuencia de una nota conociendo sus semitonos respecto a
     * otra nota de referencia (fórmula matemática conocida)
     *
     * f = f0 * 2^(n/12) siendo f0 la frecuencia de referencia (normalmente de A4/La4 440.0) y n el nº
     * de semitonos desde la nota que se calcula su frecuencia hasta la nota de referencia
     */
    fun calculateFreq(ref: Double, semitones: Int): Double{
        return ref * (2.0.pow(semitones/12.0))
    }

    /**
     * Metodo que calcula los semitonos que hay de una nota hasta la nota A4 (fórmula matemática conocida)
     * n = (octava - 4) * 12 + (índice de la nota (partiendo de A4/La4 de f=440 como referencia) - 9 (posición de La en el array))
     *
     * ¿Por qué index - 9 y no comenzar la lista por la de forma que quedara la fórmula sin la resta?
     * Porque las octavas se forman partiendo de Do, si no se tiene en cuenta las frecuencias se calculan de forma incorrecta.
     * Se mantiene de referencia La4 como referencia por estándar internacional.
     */
    fun calculateSemitone(octave: Int, index: Int): Int {
        return (octave - 4) * 12 + (index - 9);
    }
}