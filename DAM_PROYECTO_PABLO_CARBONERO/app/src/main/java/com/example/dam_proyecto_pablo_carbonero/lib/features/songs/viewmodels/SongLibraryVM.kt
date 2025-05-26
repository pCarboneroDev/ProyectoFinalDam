package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongSearchDelegate
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.sortByOption
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetAllSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongLibraryVM @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase
): ViewModel() {

    private val _songList = MutableStateFlow<List<SongWithTuning>>(emptyList())
    val songList: StateFlow<List<SongWithTuning>> = _songList

    private val _searchResults = MutableStateFlow<List<SongWithTuning>>(emptyList())
    val searchResults: StateFlow<List<SongWithTuning>> = _searchResults

    private val _selectedSortOption = MutableStateFlow<SortOption>(SortOption.DATE_ASCENDING)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        loadViewModel()
    }

    /**
     * metodo que se encarga de cambiar como se ordena la lista
     */
    fun sortList(sortOption: SortOption){
        _songList.value = _songList.value.sortByOption(sortOption)
        _selectedSortOption.value = sortOption
    }

    fun loadViewModel(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _songList.value = getAllSongsUseCase.call(Unit)
                //_searchDelegate.value = SongSearchDelegate(_songList.value)
            }
            catch (e: Exception){
                //TODO gestionar excepcion
                Log.d("AA", e.message.toString())
            }
        }
    }


    fun clear() {
        _searchQuery.value = ""
    }

    fun type(value: String){
        _searchQuery.value = value
        _searchResults.value = _songList.value.filter { it.song.name.lowercase().contains(value.lowercase()) }
    }

}