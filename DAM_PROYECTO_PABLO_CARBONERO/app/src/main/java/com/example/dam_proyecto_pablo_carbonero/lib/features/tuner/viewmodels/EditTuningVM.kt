package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.UserPreferencesRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTuningVM @Inject constructor(
    private val notesRepo: MusicNoteRepository,
    private val tuningRepo: TuningRepository,
    private val tuningMusicNoteRepo: TuningMusicNoteRepository,
    //selectedTuning: String?,
    private val savedStateHandle: SavedStateHandle,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    val selectedTuning = savedStateHandle.get<String>("selectedTuning") ?: ""

    private lateinit var tuningModel: TuningWithNotesModel

    private val _tuningName = MutableStateFlow<String>("")
    val tuningName: StateFlow<String> = _tuningName

    private val _finalTuning = MutableStateFlow<Tuning?>(null)

    private val _noteList = MutableStateFlow<List<MusicNote>>(emptyList())
    val noteList: StateFlow<List<MusicNote>> = _noteList

    private val _selectedNotes: MutableStateFlow<Array<MusicNote>> = MutableStateFlow(Array(6) { MusicNote() })
    val selectedNotes: StateFlow<Array<MusicNote>> = _selectedNotes

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes


    init {
        var list: List<MusicNote>;
        tuningModel = Gson().fromJson(selectedTuning, TuningWithNotesModel::class.java)
        _tuningName.value = tuningModel.tuning.name

        viewModelScope.launch(Dispatchers.IO) {
            try{
                list = notesRepo.getAllNotes() as MutableList<MusicNote>;
                list.sort()
                _noteList.value = list

                _latinNotes.value = preferencesRepo.getNotationPreference()
            }
            catch (e: Exception){
                //TODO Gestionar la posible excepcion
                Log.d("Error al obtener las notas", "Habrá que hacer algo")
            }
        }

        var i = _selectedNotes.value.size - 1
        for (note in tuningModel.noteList){
            _selectedNotes.value[i] = note
            i--
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
                saveTuningInBbDd()
                saveTuningNotesInBbDd(list)
            }
        }catch (e: Exception){
            saved = false
        }

        return saved
    }

    suspend fun saveTuningInBbDd() {
        try{
            tuningRepo.updateTuningName(tuningModel.tuning.id, _tuningName.value!!)
        }
        catch (e: Exception){
            throw e;
        }
    }

    suspend fun saveTuningNotesInBbDd(notes: List<MusicNote>){
        try {
            var list = mutableListOf<MusicNote>()

            for (note in notes) {
                val noteFound = notes.find { it.englishName == note.englishName }
                if (noteFound != null) {
                    list.add(noteFound)
                } else {
                    println("Nota ${note.englishName} no encontrada en la lista de notas")
                }
            }
            tuningMusicNoteRepo.deleteNoteByTuningId(tuningModel.tuning.id) //accesoBD.eliminarNotasAfinacion(tuningModel.tuning.id)
            for (note in list){
                var insert = TuningMusicNote(tuningId = tuningModel.tuning.id, noteId = note.id)
                tuningMusicNoteRepo.insertTuningMusicNote(insert) //accesoBD.insertarNotaAfinacion(insert)
            }
        }catch (e: Exception){
            //TODO Gestionar esto
            throw e;
        }
    }

    fun borrarAfinacion(){
        viewModelScope.launch(Dispatchers.IO) {
            tuningMusicNoteRepo.deleteNoteByTuningId(tuningModel.tuning.id)
            tuningRepo.deleteTuningById(tuningModel.tuning.id)
        }
    }

}