package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class CreateSongVM @Inject constructor(
    private val tuningRepo: TuningRepository,
    private val songRepo: SongRepository
): ViewModel() {
    private val _tuningList = MutableLiveData<List<Tuning>>()
    val tuningList: LiveData<List<Tuning>> = _tuningList

    private val _selectedTuning = MutableLiveData<Tuning>()
    val selectedTuning: LiveData<Tuning> = _selectedTuning

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

    private lateinit var _finalSong: Song


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val t = tuningRepo.getAllTunings()
            _tuningList.postValue(t)
        }
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
    fun saveSong(): Boolean{
        var saved = true

        try{
            _finalSong = Song(
                name = _songName.value!!,
                bandName = _bandName.value!!,
                tuningId = _selectedTuning.value!!.id,
                bpm = _bpm.value!! + " bpm",
                key = _key.value!!
            )

            viewModelScope.launch(Dispatchers.IO) {
                insertSongDdBb(_finalSong)
            }
        }catch (e: Exception){
            saved = false;
        }

        return saved;
    }

    /**
     * Metodo que se encarga de realizar la inserción de la nueva canción en la bbdd
     */
    suspend fun insertSongDdBb(song: Song){
        try{
            var id = songRepo.insertSong(song)
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