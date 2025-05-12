package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSongVM @Inject constructor(
    private val songRepository: SongRepository,
    private val tuningRepository: TuningRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val songId = savedStateHandle.get<String>("songId") ?: ""

    private val _selectedSong = MutableLiveData<Song>()
    val selectedSong: LiveData<Song> = _selectedSong

    private val _selectedTuning = MutableLiveData<Tuning>()
    val selectedTuning: LiveData<Tuning> = _selectedTuning

    private val _tuningList = MutableLiveData<List<Tuning>>()
    val tuningList: LiveData<List<Tuning>> = _tuningList

    private val _songName = MutableLiveData<String>()
    val songName: LiveData<String> = _songName

    private val _bandName = MutableLiveData<String>()
    val bandName: LiveData<String> = _bandName

    private val _bpm = MutableLiveData<String>()
    val bpm: LiveData<String> = _bpm

    private val _key = MutableLiveData<String>()
    val key: LiveData<String> = _key

    private val _formValid = MutableLiveData<Boolean>()
    val formValid: LiveData<Boolean> = _formValid

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val song = getSongFromDdBb(songId.toLong())
                _selectedSong.postValue(song)
                val tuning = getTuningFromDdBb(song.tuningId)
                _selectedTuning.postValue(tuning)
                loadData(song)
                _tuningList.postValue(tuningRepository.getAllTunings())
            }catch (e: Exception){
                // todo gestionar excepción
            }
        }
    }

    suspend fun getSongFromDdBb(id: Long): Song {
        return songRepository.getSongById(id)
    }

    suspend fun getTuningFromDdBb(id: Long): Tuning{
        return tuningRepository.getTuningById(id)
    }

    suspend fun loadData(song: Song) {
        _songName.postValue(song.name)
        _bandName.postValue(song.bandName)
        _bpm.postValue(song.bpm.substringBefore(' '))
        _key.postValue(song.key)
    }



    //SETTERS

    fun setSelectedTuning(value: Tuning){
        _selectedTuning.value = value
    }

    fun setSongName(name: String) {
        if (name.length <= 25){
            _songName.value = name
        }

    }

    fun setBandName(name: String) {
        if (name.length <= 25){
            _bandName.value = name
        }
    }

    fun setBpm(value: String) {
        if (isNumeric(value)){
            _bpm.value = value

            if (_bpm.value!!.toDouble() > 400){
                _bpm.value = "400"
            }
        }
        if (value == "") _bpm.value = ""
    }

    fun setKey(value: String) {
        _key.value = value
    }

    /**
     * Metodo para guardar una canción en la bbdd
     * return: true si se ha guaradado, false si hubo un error.
     */
    suspend fun saveSong(): Boolean{
        var saved = true

        try{
            var s = Song(
                id = _selectedSong.value!!.id,
                name = _songName.value!!,
                bandName = _bandName.value!!,
                tuningId = _selectedTuning.value!!.id,
                bpm = _bpm.value!! + " bpm",
                key = _key.value!!
            )


            updateSong(s)
        }catch (e: Exception){
            saved = false;
            Log.d("ERROu", e.message.toString());
        }
        return saved
    }

    /**
     * Metodo que se encarga de realizar la inserción de la nueva canción en la bbdd
     */
    suspend fun updateSong(song: Song){
        try{
            var rowsAffected = songRepository.updateSong(song)
        }catch (e: Exception){
            throw e
        }
    }

    fun isSongNameValid(): Boolean {
        return _songName.value.isNullOrEmpty()
    }

    fun isBandNameValid(): Boolean {
        return _bandName.value.isNullOrEmpty()
    }

    fun isBpmValid(): Boolean {
        return _bpm.value.isNullOrEmpty()
    }

    fun isNumeric(numericText: String): Boolean{
        return numericText.toDoubleOrNull() != null
    }

    fun isFormValid() {
        _formValid.value = (!isSongNameValid() || !isBandNameValid() || !isBpmValid() || _selectedTuning.value == null)
    }
}