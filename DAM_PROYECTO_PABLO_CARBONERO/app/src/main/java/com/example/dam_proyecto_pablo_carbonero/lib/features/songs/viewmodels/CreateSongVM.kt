package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.InsertSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningUseCases.GetAllTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.InvalidFormException
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSongVM @Inject constructor(
    private val getAllTuningUseCase: GetAllTuningUseCase,
    private val insertSongUseCase: InsertSongUseCase
): ViewModel() {
    private val _tuningList = MutableStateFlow<List<Tuning>>(emptyList())
    val tuningList: StateFlow<List<Tuning>> = _tuningList.asStateFlow()

    private val _selectedTuning = MutableStateFlow<Tuning?>(null)
    val selectedTuning: StateFlow<Tuning?> = _selectedTuning

    private val _songName = MutableStateFlow<String>("")
    val songName: StateFlow<String> = _songName

    private val _bandName = MutableStateFlow<String>("")
    val bandName: StateFlow<String> = _bandName

    private val _bpm = MutableStateFlow<String>("")
    val bpm: StateFlow<String> = _bpm

    private val _tabs = MutableStateFlow<String>("")
    val tabs: StateFlow<String> = _tabs

    private lateinit var _finalSong: SongWithTuning

    //Dialog
    private val _tabsModal = MutableStateFlow<Boolean>(false)
    val tabsModal: StateFlow<Boolean> = _tabsModal

    // Message manager
    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true))
    val messageManager: StateFlow<MessageManager> = _messageManager


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val t = getAllTuningUseCase.call(Unit)
            _tuningList.value = t
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

            if (_bpm.value.toDouble() > 400){
                _bpm.value = "400"
            }
        }
        if (value == "") _bpm.value = ""
    }

    fun setTabs(value: String) {
        _tabs.value = value
    }

    fun setTabsModal(value: Boolean){
        _tabsModal.value = value
    }

    /**
     * Metodo para guardar una canción en la bbdd
     * @return: true si se ha guaradado, false si hubo un error.
     */
    suspend fun saveSong(): Boolean{
        var saved = true

        try{
            if(!isFormValid()){
                throw InvalidFormException("Complete all the fields")
            }
            val song = Song(
                name = _songName.value,
                bandName = _bandName.value,
                tuningId = _selectedTuning.value!!.id,
                bpm = _bpm.value + " bpm",
                tabs = _tabs.value
            )
            _finalSong = SongWithTuning(song = song, tuning = _selectedTuning.value!!)
            val id = insertSongUseCase.call(_finalSong)
            if(id == 0L) {
                saved = false
                _messageManager.value = MessageManager(false)
            }
        }
        catch (form: InvalidFormException){
            saved = false
            _messageManager.value = MessageManager(false, form.message.toString())
        }
        catch (e: Exception){
            saved = false;
            _messageManager.value = MessageManager(false)
        }

        return saved
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

    /**
     * Comprueba que todos los campos cumplen los requisitos
     */
    fun isFormValid(): Boolean {
        return (!isSongNameValid() && !isBandNameValid() && !isBpmValid() && _selectedTuning.value != null)
    }

    fun resetMessageManager() {
        _messageManager.value = MessageManager(true)
    }
}