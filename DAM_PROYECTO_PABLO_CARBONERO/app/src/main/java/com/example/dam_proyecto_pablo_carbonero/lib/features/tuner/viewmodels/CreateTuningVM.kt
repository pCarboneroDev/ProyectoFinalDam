package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTuningVM @Inject constructor(
    private val notesRepo: MusicNoteRepository,
    private val tuningRepo: TuningRepository,
    private val tuningMusicNoteRepo: TuningMusicNoteRepository,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    private val _tuningName = MutableLiveData<String>()
    val tuningName: LiveData<String> = _tuningName

    private val _finalTuning = MutableLiveData<Tuning>()

    private val _noteList = MutableLiveData<List<MusicNote>>()
    val noteList: LiveData<List<MusicNote>> = _noteList

    private val _selectedNotes: MutableLiveData<Array<MusicNote>> = MutableLiveData(Array(6) { MusicNote() })
    val selectedNotes: LiveData<Array<MusicNote>> = _selectedNotes

    private val _latinNotes = MutableLiveData<Boolean>()
    val latinNotes: LiveData<Boolean> = _latinNotes


    init {
        var list: MutableList<MusicNote>
        viewModelScope.launch(Dispatchers.IO) {
            try{
                list = notesRepo.getAllNotes() as MutableList<MusicNote>;
                list.sort()
                _noteList.postValue(list);

                _latinNotes.postValue(preferencesRepo.getNotationPreference())
            }
            catch (e: Exception){
                //TODO Gestionar la posible excepcion
                Log.d("Error al obtener las notas", "Habrá que hacer algo")
            }
        }
    }


    // SETTERS
    fun setTuningName(value: String){
        _tuningName.value = value
    }


    // METODOS
    fun saveNewTuning(): Boolean{
        var saved = true

        try{
            var list = selectedNotes.value!!.toList() as MutableList<MusicNote>
            list.sort();

            _finalTuning.value = Tuning(name = _tuningName.value.toString())

            viewModelScope.launch(Dispatchers.IO) {
                var newId = saveTuningInBbDd()
                saveTuningNotesInBbDd(newId, list)
            }
        }catch (e: Exception){
            saved = false
        }

        return saved
    }

    suspend fun saveTuningInBbDd(): Long{
        var newId: Long = 0
        try{
            newId = tuningRepo.insertTuning(_finalTuning.value!!)
        }
        catch (e: Exception){
            throw e;
        }
        return newId
    }

    suspend fun saveTuningNotesInBbDd(id: Long, notes: List<MusicNote>){
        try {
            for (note in notes) {
                val insert = TuningMusicNote(tuningId = id, noteId = note.id)
                tuningMusicNoteRepo.insertTuningMusicNote(insert)
            }

            /*var lista = mutableListOf<MusicNote>()

            for (nota in notes) {
                val noteFound = notes.find { it.englishName == nota.englishName }
                if (noteFound != null) {
                    lista.add(noteFound)
                } else {
                    println("Nota ${nota.englishName} no encontrada en la lista de notas")
                }
            }

            for (note in lista){
                var insert = TuningMusicNote(tuningId = id, noteId = note.id)
                tuningMusicNoteRepo.insertTuningMusicNote(insert)
            }*/
        }catch (e: Exception){
            tuningRepo.deleteTuningById(id)
            throw e;
        }
    }
}