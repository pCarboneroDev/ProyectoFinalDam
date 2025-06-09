package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.InsertTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.InvalidFormException
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Array

@HiltViewModel
class CreateTuningVM @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val insertTuningUseCase: InsertTuningUseCase,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _tuningName = MutableStateFlow<String>("")
    val tuningName: StateFlow<String> = _tuningName

    private val _noteList = MutableStateFlow<List<MusicNote>>(emptyList())
    val noteList: StateFlow<List<MusicNote>> = _noteList

    private val _selectedNotes: MutableStateFlow<Array<MusicNote>> = MutableStateFlow(Array(6) { MusicNote() })
    val selectedNotes: StateFlow<Array<MusicNote>> = _selectedNotes

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes

    private val _finalTuning = MutableStateFlow<Tuning?>(null)

    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true, ""))
    val messageManager: StateFlow<MessageManager> = _messageManager


    init {
        var list: MutableList<MusicNote>
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try{
                list = getAllNotesUseCase.call(Unit).toMutableList()
                list.sort()
                _noteList.value = list
                Log.d("FILTER", "Resultados: ${_noteList.value.map { it.englishName }}")
            }catch (e: Exception){
                _messageManager.value = MessageManager(false, "Unexpected error loading data.")
            }
            _latinNotes.value = preferencesRepo.getNotationPreference()
            _isLoading.value = false
        }
    }

    // SETTERS
    fun setTuningName(value: String){
        _tuningName.value = value
    }

    // METODOS
    suspend fun saveNewTuning(): Boolean{
        _isLoading.value = true
        var saved = true
        try{
            if (!isFormValid()){
                throw InvalidFormException("Complete all the field")
            }
            _finalTuning.value = Tuning(name = _tuningName.value)
            val list = _selectedNotes.value.toMutableList()
            list.sort()

            insertTuningUseCase.call(TuningWithNotesModel(_finalTuning.value!!, list))
        }
        catch (formE: InvalidFormException){
            saved = false
            _isLoading.value = false
            _messageManager.value = MessageManager(false, formE.message.toString())
        }
        catch (e: Exception){
            saved = false
            _isLoading.value = false
            _messageManager.value = MessageManager(false)
        }
        _isLoading.value = false
        return saved
    }

    private fun isFormValid(): Boolean{
        var isValid = true

        if (_tuningName.value.isEmpty()){
            isValid = false
        }

        for(note in _selectedNotes.value){
            if(note.englishName == "0"){
                isValid = false
            }
        }
        return isValid
    }

    fun resetMessageManager(){
        _messageManager.value = MessageManager(true)
    }
}

