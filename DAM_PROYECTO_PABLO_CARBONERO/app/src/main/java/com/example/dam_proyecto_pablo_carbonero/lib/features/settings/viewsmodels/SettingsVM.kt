package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.repositories_impl.UserPreferencesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val prefRepo: UserPreferencesRepositoryImpl
): ViewModel() {

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _latinNotes.value = prefRepo.getNotationPreference()
        }
    }


    // MÉTODOS
    fun setNotationValue(value: Boolean){
        _latinNotes.value = value
        viewModelScope.launch(Dispatchers.IO) {
            prefRepo.setNotationPreference(value)
        }
    }
}