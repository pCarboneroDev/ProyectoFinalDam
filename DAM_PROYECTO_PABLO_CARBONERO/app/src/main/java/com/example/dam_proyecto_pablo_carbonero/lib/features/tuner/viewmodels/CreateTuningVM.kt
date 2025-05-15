package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.InsertTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
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
    private val _tuningName = MutableStateFlow<String>("")
    val tuningName: StateFlow<String> = _tuningName

    private val _noteList = MutableStateFlow<List<MusicNote>>(emptyList())
    val noteList: StateFlow<List<MusicNote>> = _noteList

    private val _selectedNotes: MutableStateFlow<Array<MusicNote>> = MutableStateFlow(Array(6) { MusicNote() })
    val selectedNotes: StateFlow<Array<MusicNote>> = _selectedNotes

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes

    private val _finalTuning = MutableStateFlow<Tuning?>(null)


    init {
        var list: MutableList<MusicNote>
        viewModelScope.launch(Dispatchers.IO) {
            try{
                list = getAllNotesUseCase.call(Unit).toMutableList();
                list.sort()
                _noteList.value = list
            }catch (e: Exception){
                //TODO Gestionar la posible excepcion
            }
            _latinNotes.value = preferencesRepo.getNotationPreference()
        }
    }

    // SETTERS
    fun setTuningName(value: String){
        _tuningName.value = value
    }

    // METODOS
    suspend fun saveNewTuning(): Boolean{
        var saved = true
        try{
            _finalTuning.value = Tuning(name = _tuningName.value)
            val list = _selectedNotes.value.toMutableList()
            list.sort()

            insertTuningUseCase.call(TuningWithNotesModel(_finalTuning.value!!, list))

        }catch (e: Exception){
            saved = false
        }
        return saved
    }
}