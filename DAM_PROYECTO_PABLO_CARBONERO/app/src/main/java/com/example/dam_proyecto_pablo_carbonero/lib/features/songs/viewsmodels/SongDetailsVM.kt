package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.models.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.models.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongDetailsVM @Inject constructor(
    private val songRepository: SongRepository,
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository,
    private val musicNoteRepository: MusicNoteRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(){
    //val songSerialized = savedStateHandle.get<String>("selectedSong") ?: ""
    val songId = savedStateHandle.get<String>("songId") ?: ""
    val tuningId = savedStateHandle.get<String>("tuningId") ?: ""

    private val _selectedSong = MutableStateFlow<SongWithTuning?>(null)
    val selectedSong: StateFlow<SongWithTuning?> = _selectedSong

    private val _songName = MutableStateFlow<String>("")
    val songName: StateFlow<String> = _songName

    init {
        //_selectedSong.value = Gson().fromJson(songSerialized, SongWithTuning::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            val song = songRepository.getSongById(songId.toLong())
            val tuning = tuningRepository.getTuningById(tuningId.toLong())
            val noteIdList = tuningMusicNoteRepository.getNotesIdFromTuningId(tuningId.toLong())
            val noteList = mutableListOf<MusicNote>()

            for(id in noteIdList){
                val note = musicNoteRepository.getMusicNoteById(id)
                noteList.add(note)
            }

            noteList.sort()

            val songTuning = SongWithTuning(tuning, song, noteList)
            _selectedSong.value = songTuning
        }
    }
}