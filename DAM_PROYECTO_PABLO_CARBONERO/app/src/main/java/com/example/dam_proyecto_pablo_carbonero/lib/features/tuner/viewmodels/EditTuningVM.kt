package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.DeleteTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetTuningByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTuningVM @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getTuningByIdUseCase: GetTuningByIdUseCase,
    private val updateTuningUseCase: UpdateTuningUseCase,
    private val deleteTuningUseCase: DeleteTuningUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val preferencesRepo: UserPreferencesRepository
): ViewModel() {
    val selectedTuningId = savedStateHandle.get<String>("selectedTuningId") ?: ""

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
        viewModelScope.launch(Dispatchers.IO) {
            try{
                //tuningModel = Gson().fromJson(selectedTuningId, TuningWithNotesModel::class.java)
                tuningModel = getTuningByIdUseCase.call(selectedTuningId.toLong())
                _tuningName.value = tuningModel.tuning.name

                var list: List<MusicNote> = getAllNotesUseCase.call(Unit);

                _noteList.value = list
                _latinNotes.value = preferencesRepo.getNotationPreference()
            }
            catch (e: Exception){
                //TODO Gestionar la posible excepcion
            }
            var i = _selectedNotes.value.size - 1
            for (note in tuningModel.noteList){
                _selectedNotes.value[i] = note
                i--
            }
        }
    }



    // SETTERS
    fun setTuningName(value: String){
        _tuningName.value = value
    }


    // METODOS
    suspend fun updateTuning(): Boolean{
        var saved = true
        try{
            var list = selectedNotes.value.toList() as MutableList<MusicNote>
            list.sort()

            _finalTuning.value = Tuning( id = selectedTuningId.toLong() ,name = _tuningName.value.toString())

            saved = updateTuningUseCase.call(TuningWithNotesModel(_finalTuning.value!!, list))
        }catch (e: Exception){
            Log.d("ERROU", e.message.toString())
            saved = false
        }
        return saved
    }


    fun borrarAfinacion(){
        viewModelScope.launch(Dispatchers.IO) {
            var list = _selectedNotes.value.toMutableList()
            list.sort()
            deleteTuningUseCase.call(TuningWithNotesModel(tuningModel.tuning, list))

        }
    }

}