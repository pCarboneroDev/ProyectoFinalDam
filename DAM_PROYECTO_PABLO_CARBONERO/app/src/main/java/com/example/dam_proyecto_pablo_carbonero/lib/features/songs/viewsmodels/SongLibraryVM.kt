package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.sortByOption
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.models.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class SongLibraryVM @Inject constructor(
    private val songRepo: SongRepository,
    private val tuningRepo: TuningRepository
): ViewModel() {

    private val _songList = MutableStateFlow<List<SongWithTuning>>(emptyList())
    val songList: StateFlow<List<SongWithTuning>> = _songList

    private val _selectedSortOption = MutableStateFlow<SortOption>(SortOption.DATE_ASCENDING)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption


    init {
        loadViewModel()
    }

    /**
     * metodo que se encarga de cambiar como se ordena la lista
     */
    fun sortList(sortOption: SortOption){
        _songList.value = _songList.value!!.sortByOption(sortOption)
        _selectedSortOption.value = sortOption
    }

    fun loadViewModel(){
        viewModelScope.launch(Dispatchers.IO) {
            var list = songRepo.getAllSongs()

            var songModelList = mutableListOf<SongWithTuning>()

            for (song in list){
                var t = tuningRepo.getTuningById(song.tuningId)
                songModelList.add(SongWithTuning(t, song))
            }

            _songList.value = songModelList
        }
    }

}