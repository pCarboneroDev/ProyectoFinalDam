package com.example.dam_proyecto_pablo_carbonero.lib.features.loadingScreen.viewsmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.InsertAllMusicNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningUseCases.GetAllTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.InsertTuningUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt

@HiltViewModel
class LoadingVM @Inject constructor (
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getAllTuning: GetAllTuningUseCase,
    private val insertAllMusicNotesUseCase: InsertAllMusicNotesUseCase,
    private val insertTuningUseCase: InsertTuningUseCase
): ViewModel() {

    private val _loadComplete = MutableStateFlow(false)
    val loadComplete: StateFlow<Boolean> = _loadComplete


    private val _txtPlaceholder = MutableStateFlow<String>("")
    val txtPlaceholder: StateFlow<String> = _txtPlaceholder

    // esto se ejecuta al instanciar el viewModel
    init {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                var notes = getAllNotesUseCase.call(Unit)
                val tunings = getAllTuning.call(Unit)

                if (notes.isEmpty()) {
                    notes = generateMusicNotes()
                }
                if(tunings.isEmpty()){
                    generateStandardTuning(notes)
                }
                _loadComplete.value = true
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

        insertAllMusicNotesUseCase.call(notesListToInsert)
        return getAllNotesUseCase.call(Unit)
    }

    /**
     * Metodo que genera la afinación básica para el funcionamiento de la app
     */
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

        var standardTunning = Tuning(name = "Standard Tuning")

        val tuningwithNotes = TuningWithNotesModel(tuning = standardTunning, noteList = list)

        var standardId = insertTuningUseCase.call(tuningwithNotes)//tuningRepo.insertTuning(standardTunning)
    }


    // Metodo que recorre todas las notas musicales y crea todas las necesarias para el funcionamiento de la app
    fun getFreqAccordingOnA4(): List<MusicNote> {
        var freqRef: Double = 440.0
        var index = 0

        var noteList = listOf<String>("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#","A", "A#", "B")


        var englishNames = listOf<String>("A", "B", "C", "D", "E", "F", "G")
        var latinNames = listOf<String>("La", "Si", "Do", "Re", "Mi", "Fa", "Sol")

        val sharpToFlatMap = mapOf(
            "C#" to "Db",
            "D#" to "Eb",
            "F#" to "Gb",
            "G#" to "Ab",
            "A#" to "Bb"
        )

        var musicNoteList = mutableListOf<MusicNote>();

        for (note in noteList){
            for (i in 1..4){
                var octave = i;
                var semitone = calculateSemitone(octave, noteList.indexOf(note))
                var freqWanted = calculateFreq(freqRef, semitone);
                var indexN = englishNames.indexOf(note[0].toString())

                val sharp = if (note.contains("#")) "#" else null
                val bemol = if (note.contains("#")) sharpToFlatMap[note] + i else null


                val cents = 10
                val marginRatio = 2.0.pow(cents / 1200.0)

                val id = freqWanted.roundToInt().toLong()

                var nota = MusicNote(
                    id = id,
                    latinNotation = latinNames[indexN],
                    englishNotation = englishNames[indexN],
                    octave = octave,
                    maxHz = freqWanted * marginRatio,
                    minHz = freqWanted / marginRatio,
                    sharpIndicator = sharp,
                    alternativeName = bemol
                )
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