package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAllTuningWithNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateFavouriteUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.FullFavouriteTuningsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserTuningsVM @Inject constructor(
    private val getAllTuningWithNotesUseCase: GetAllTuningWithNotesUseCase,
    private val updateFavouriteUseCase: UpdateFavouriteUseCase
): ViewModel() {

    private val _tuningList = MutableStateFlow<List<TuningWithNotesModel>>(emptyList())
    val tuningList: StateFlow<List<TuningWithNotesModel>> = _tuningList

    init {
        viewModelScope.launch(Dispatchers.IO){
            loadTunings()
        }
    }

    suspend fun setTuningAsFavourite(tuning: TuningWithNotesModel): Pair<Boolean, String> {
        var updated = true
        var message = ""
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
            updated = false
            message = favE.message.toString()
        }
        catch (e: Exception){
            updated = false
            message = "Unexpected error. Try again later"
        }
        return Pair(updated, message)
    }

    suspend fun loadTunings(){
        _tuningList.value = getAllTuningWithNotesUseCase.call(Unit)
    }
}