package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.sortByOption
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetAllSongsUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetPagedSongsUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.SearchSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetTuningByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongLibraryVM @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase,
    private val getPagedSongsUseCase: GetPagedSongsUseCase,
    private val getTuningByIdUseCase: GetTuningByIdUseCase,
    private val searchSongUseCase: SearchSongUseCase
): ViewModel() {

    private val _songList = MutableStateFlow<List<SongWithTuning>>(emptyList())
    val songList: StateFlow<List<SongWithTuning>> = _songList

    private val _selectedSortOption = MutableStateFlow<SortOption>(SortOption.DATE_ASCENDING)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    private val _isSearchBarOpen = MutableStateFlow<Boolean>(false)
    val isSearchBarOpen: StateFlow<Boolean> = _isSearchBarOpen

    private val _isModalOpen = MutableStateFlow<Boolean>(false)
    val isModalOpen: StateFlow<Boolean> = _isSearchBarOpen


    @OptIn(ExperimentalCoroutinesApi::class)
    // flatmap lo que hace es que cada vez que el elemento al cual se escucha (sortOption) emite un valor se emite un nuevo flow  del paging
    val songListPaged: Flow<PagingData<SongWithTuning>> = _selectedSortOption.flatMapLatest { sortOption ->
        Pager(
            config = PagingConfig(pageSize = 3, prefetchDistance = 0, initialLoadSize = 3),
            pagingSourceFactory = { getPagedSongsUseCase.synchronousCall(sortOption) }
        ).flow.map { pagingData ->
            pagingData.map { song -> // el map este permite métodos asincrónicos parece
                val tuning = getTuningByIdUseCase.call(song.tuningId)
                SongWithTuning(song = song, tuning = tuning.tuning)
            }
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResultsPaged: Flow<PagingData<SongWithTuning>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if(query.isEmpty()){
                flowOf(PagingData.empty())
            }
            else{
                Pager(
                    config = PagingConfig(pageSize = 5, prefetchDistance = 1, initialLoadSize = 5),
                    pagingSourceFactory = { searchSongUseCase.synchronousCall(query) }
                ).flow.map { pagingData ->
                    pagingData.map { song ->
                        val tuning = getTuningByIdUseCase.call(song.tuningId)
                        SongWithTuning(song = song, tuning = tuning.tuning)
                    }
                }
            }
                .cachedIn(viewModelScope)
        }


    init {
        loadViewModel()
    }

    /**
     * metodo que se encarga de cambiar como se ordena la lista
     */
    fun sortList(sortOption: SortOption){
        _selectedSortOption.value = sortOption

        //loadViewModel()

        Log.d("SortOption: ", "${_selectedSortOption.value}")
    }

    /**
     * Metodo que carga los datos necesarios para el viewmodel.
     * Se separa en una función independiente ya que se tendrá que llamar desde la vista en algunos momentos
     */
    fun loadViewModel(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
        _query.value = ""
        onQueryChanged("")
    }

    /**
     * Se encarga de filtrar la lista de la barra de búsqueda y de actualizar el texto
     */
    fun onQueryChanged(value: String){
        _query.value = value
    }
}

