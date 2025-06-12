package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.DeleteSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetPagedSongsUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.SearchSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetTuningByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import com.example.dam_proyecto_pablo_carbonero.lib.utils.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SongLibraryVM @Inject constructor(
    private val getPagedSongsUseCase: GetPagedSongsUseCase,
    private val getTuningByIdUseCase: GetTuningByIdUseCase,
    private val searchSongUseCase: SearchSongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase
): ViewModel() {

    private val _selectedSortOption = MutableStateFlow<SortOption>(SortOption.DATE_ASCENDING)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption

    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    private val _isSearchBarOpen = MutableStateFlow<Boolean>(false)
    val isSearchBarOpen: StateFlow<Boolean> = _isSearchBarOpen

    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true, ""))
    val messageManager: StateFlow<MessageManager> = _messageManager

    private val _sortModal = MutableStateFlow<Boolean>(false)
    val sortModal: StateFlow<Boolean> = _sortModal


    fun setSortModal(value: Boolean){
        _sortModal.value = value
    }

    fun setSearchbar(value: Boolean){
        _isSearchBarOpen.value = value
    }


    // recibe la lista de canciones gestionada por paging
    @OptIn(ExperimentalCoroutinesApi::class)
    // flatmap lo que hace es que cada vez que el elemento al cual se escucha (sortOption) emite un valor se emite un nuevo flow  del paging
    val songListPaged: Flow<PagingData<SongWithTuning>> = _selectedSortOption.flatMapLatest { sortOption ->
        Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1, initialLoadSize = 20),
            pagingSourceFactory = { getPagedSongsUseCase.synchronousCall(sortOption) }
        ).flow.map { pagingData ->
            pagingData.map { song -> // el map este permite métodos asincrónicos parece
                //se convierte los objetos de SOng a SongWithTuning
                val tuning = getTuningByIdUseCase.call(song.tuningId)
                SongWithTuning(song = song, tuning = tuning.tuning)
            }
        }
    }.cachedIn(viewModelScope)

    // recibe la lista de canciones gestionada por paging
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


    /**
     * metodo que se encarga de cambiar como se ordena la lista
     */
    fun sortList(sortOption: SortOption){
        _selectedSortOption.value = sortOption

        //loadViewModel()

        Log.d("SortOption: ", "${_selectedSortOption.value}")
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

    /**
     * llama al caso de uso para borrar la canción deseada
     */
    suspend fun deleteSong(songId: Long){
        try{
            deleteSongUseCase.call(songId)
        }catch (e: Exception){
            _messageManager.value = MessageManager(false)
        }
    }

    /**
     * resetea el valor de resetManager
     */
    fun resetMessageManager(){
        _messageManager.value = MessageManager(true, "")
    }
}

