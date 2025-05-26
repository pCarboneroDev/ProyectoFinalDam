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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongLibraryVM @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase
): ViewModel() {

    private val _songList = MutableStateFlow<List<SongWithTuning>>(emptyList())
    val songList: StateFlow<List<SongWithTuning>> = _songList

    private val _selectedSortOption = MutableStateFlow<SortOption>(SortOption.DATE_ASCENDING)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearchBarOpen = MutableStateFlow<Boolean>(false)
    val isSearchBarOpen: StateFlow<Boolean> = _isSearchBarOpen

    private val _isModalOpen = MutableStateFlow<Boolean>(false)
    val isModalOpen: StateFlow<Boolean> = _isSearchBarOpen

    val searchResults = combine(songList, searchQuery) { list, query ->
        if (query.isBlank()) emptyList<SongWithTuning>()
        else list.filter { it.song.name.contains(query, ignoreCase = true) || it.song.bandName.contains(query, ignoreCase = true) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )



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

    /**
     * Metodo que carga los datos necesarios para el viewmodel.
     * Se separa en una función independiente ya que se tendrá que llamar desde la vista en algunos momentos
     */
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

    /**
     * Limpia la barra de búsqueda
     */
    fun clearQuery() {
        _searchQuery.value = ""
        onQueryChanged("")
    }

    /**
     * Se encarga de filtrar la lista de la barra de búsqueda y de actualizar el texto
     */
    fun onQueryChanged(value: String){
        _searchQuery.value = value
        Log.d("DEBUG", "Query actual: '${_searchQuery.value}'")
        val filtered = searchResults.value
        Log.d("FILTER", "Resultados: ${filtered.map { it.song.name }}")
        /*if(value.isNotEmpty()){
            _searchResults.value = _songList.value.filter { it.song.name.lowercase().contains(value.lowercase()) }
        }
        else{
            _searchResults.value = emptyList<SongWithTuning>()
        }*/

    }

}