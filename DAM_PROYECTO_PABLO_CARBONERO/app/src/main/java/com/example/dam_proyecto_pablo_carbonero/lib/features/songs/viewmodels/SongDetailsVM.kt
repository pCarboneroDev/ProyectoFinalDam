package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.DeleteSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetSongByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetSongWithTuningByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongDetailsVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val getSongByIdUseCase: GetSongWithTuningByIdUseCase
): ViewModel(){
    //val songSerialized = savedStateHandle.get<String>("selectedSong") ?: ""
    val songId = savedStateHandle.get<String>("songId") ?: ""
    val tuningId = savedStateHandle.get<String>("tuningId") ?: ""

    private val _selectedSong = MutableStateFlow<SongWithTuning?>(null)
    val selectedSong: StateFlow<SongWithTuning?> = _selectedSong

    private val _songName = MutableStateFlow<String>("")
    val songName: StateFlow<String> = _songName

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _selectedSong.value = getSongByIdUseCase.call(songId.toLong())
                _latinNotes.value = userPreferencesRepository.getNotationPreference()
            }catch (e: Exception){
                // todo algo yea
            }
        }
    }
}