package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.DeleteTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAllTuningWithNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateFavouriteUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.FullFavouriteTuningsException
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserTuningsVM @Inject constructor(
    private val getAllTuningWithNotesUseCase: GetAllTuningWithNotesUseCase,
    private val updateFavouriteUseCase: UpdateFavouriteUseCase,
    private val deleteTuningUseCase: DeleteTuningUseCase
): ViewModel() {

    private val _tuningList = MutableStateFlow<List<TuningWithNotesModel>>(emptyList())
    val tuningList: StateFlow<List<TuningWithNotesModel>> = _tuningList

    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true, ""))
    val messageManager: StateFlow<MessageManager> = _messageManager

    init {
        viewModelScope.launch(Dispatchers.IO){
            loadTunings()
        }
    }

    /**
     * metodo que llama al usecase para cambiar el estado de favorito de una afinación
     * @param tuning la afinación que se actualiza
     */
    suspend fun setTuningAsFavourite(tuning: TuningWithNotesModel) {
        try{
            updateFavouriteUseCase.call(
                Tuning(
                    id = tuning.tuning.id,
                    name = tuning.tuning.name,
                    favourite = !tuning.tuning.favourite
                )
            )
            loadTunings()
        }
        catch (favE: FullFavouriteTuningsException){
            _messageManager.value = MessageManager(false, favE.message.toString())
        }
        catch (e: Exception){
            _messageManager.value = MessageManager(false)
        }
    }

    /**
     * llama al caso de uso para cargar la lista de afinaciones
     */
    suspend fun loadTunings(){
        _tuningList.value = getAllTuningWithNotesUseCase.call(Unit)
    }

    /**
     * Llama al usecase para borrar una afinación
     */
    suspend fun deleteTuning(tuning: TuningWithNotesModel): Boolean{
        var saved = false
        try {
            var list = tuning.noteList.toMutableList()
            list.sort()
            val rowsDeleted = deleteTuningUseCase.call(TuningWithNotesModel(tuning.tuning, list))
            saved = rowsDeleted > 0
            _tuningList.value = _tuningList.value.toMutableList().apply {
                remove(tuning)
            }
        }catch (e: Exception){
            saved = false
            _messageManager.value = MessageManager(false)
        }
        return saved
    }

    /**
     * reseta el valor del MessageManager para su funcionamiento
     */
    fun resetMessageManager(){
        _messageManager.value = MessageManager(true)
    }
}