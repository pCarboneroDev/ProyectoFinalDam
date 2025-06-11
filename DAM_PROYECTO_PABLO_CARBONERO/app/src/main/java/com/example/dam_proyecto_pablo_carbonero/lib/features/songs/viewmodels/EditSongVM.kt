package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.DeleteSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetSongByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.UpdateSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases.GetAllTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.InvalidFormException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSongVM @Inject constructor(
    private val getSongByIdUseCase: GetSongByIdUseCase,
    private val getAllTuning: GetAllTuningUseCase,
    private val updateSongUseCase: UpdateSongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val songId = savedStateHandle.get<String>("songId") ?: ""

    private val _selectedSong = MutableStateFlow<Song?>(null)
    val selectedSong: StateFlow<Song?> = _selectedSong

    private val _selectedTuning = MutableStateFlow<Tuning?>(null)
    val selectedTuning: StateFlow<Tuning?> = _selectedTuning

    private val _tuningList = MutableStateFlow<List<Tuning>>(emptyList())
    val tuningList: StateFlow<List<Tuning>> = _tuningList

    private val _songName = MutableStateFlow<String>("")
    val songName: StateFlow<String> = _songName

    private val _bandName = MutableStateFlow<String>("")
    val bandName: StateFlow<String> = _bandName

    private val _bpm = MutableStateFlow<String>("")
    val bpm: StateFlow<String> = _bpm

    private val _tabs = MutableStateFlow<String>("")
    val tabs: StateFlow<String> = _tabs


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _selectedSong.value = getSongByIdUseCase.call(songId.toLong())
                _tuningList.value = getAllTuning.call(Unit)

                val tuning = _tuningList.value.find { it.id == _selectedSong.value!!.tuningId }
                _selectedTuning.value = tuning

                loadData(_selectedSong.value!!)
            }catch (e: Exception){
                // todo gestionar excepción
            }
        }
    }



    fun loadData(song: Song) {
        _songName.value = song.name
        _bandName.value = song.bandName
        _bpm.value = song.bpm.substringBefore(' ')
        _tabs.value = song.tabs
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

            if (_bpm.value.toDouble() > 400){
                _bpm.value = "400"
            }
        }
        if (value == "") _bpm.value = ""
    }

    fun setKey(value: String) {
        _tabs.value = value
    }

    /**
     * Metodo para guardar una canción en la bbdd
     * return: true si se ha guaradado, false si hubo un error.
     */
    suspend fun saveSong(): Pair<Boolean, String>{
        var saved = true
        var message = ""

        try{
            if(!isFormValid()){
                throw InvalidFormException("Complete all the fields")
            }
            var s = Song(
                id = _selectedSong.value!!.id,
                name = _songName.value,
                bandName = _bandName.value,
                tuningId = _selectedTuning.value!!.id,
                bpm = _bpm.value + " bpm",
                tabs = _tabs.value
            )
            updateSong(s)
        }
        catch (form: InvalidFormException){
            saved = false
            message = form.message.toString()
        }
        catch (e: Exception){
            saved = false;
            message = "Unexpected error. Try again later"
        }
        return Pair(saved, message)
    }

    /**
     * Metodo que se encarga de realizar la inserción de la nueva canción en la bbdd
     */
    suspend fun updateSong(song: Song){
        try{
            var rowsAffected = updateSongUseCase.call(song)
        }catch (e: Exception){
            throw e
        }
    }

    suspend fun deleteSong(){
        try{
            deleteSongUseCase.call(songId.toLong())
        }catch (e: Exception){
            //todo gestionar excepcion
        }
    }

    fun isSongNameValid(): Boolean {
        return _songName.value.isEmpty()
    }

    fun isBandNameValid(): Boolean {
        return _bandName.value.isEmpty()
    }

    fun isBpmValid(): Boolean {
        return _bpm.value.isEmpty()
    }

    fun isNumeric(numericText: String): Boolean{
        return numericText.toDoubleOrNull() != null
    }

    fun isFormValid(): Boolean {
        return (!isSongNameValid() && !isBandNameValid() && !isBpmValid() && _selectedTuning.value != null)
    }
}