package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.UserPreferencesRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateBackupUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DeleteAccountUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DeleteCloudDataUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DownloadBackupUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.GetDatesInfoUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val prefRepo: UserPreferencesRepositoryImpl,
    private val createBackupUseCase: CreateBackupUseCase,
    private val downloadBackupUseCase: DownloadBackupUseCase,
    private val getDatesInfoUseCase: GetDatesInfoUseCase,
    private val deleteCloudDataUseCase: DeleteCloudDataUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {
    private val _auth: FirebaseAuth = Firebase.auth

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes

    private val _loggedIn = MutableStateFlow<Boolean>(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn


    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _localDate = MutableStateFlow<String>("")
    val localDate: StateFlow<String> = _localDate

    private val _cloudDate = MutableStateFlow<String>("")
    val cloudDate: StateFlow<String> = _cloudDate


    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true, ""))
    val messageManager: StateFlow<MessageManager> = _messageManager

    init {
        viewModelScope.launch(Dispatchers.Main) {
            loadViewmodel()
            _isLoading.value = false
        }
    }

    /**
     * Carga todos los datos necesarios para el funcionamiento correcto de la pantalla
     */
    suspend fun loadViewmodel() {
        _latinNotes.value = prefRepo.getNotationPreference()

        var user = _auth.currentUser

        Log.d("USER LOGGEDIN: ", "${user != null}")

        if (user != null) {
            _loggedIn.value = true
        }
    }

    suspend fun loadBackUpInfo() {
        try{
            _isLoading.value = true
            val list = getDatesInfoUseCase.call(Unit)
            _localDate.value = list[0]
            _cloudDate.value = list[1]
            _isLoading.value = false
        }
        catch (e: NullPointerException){
            _localDate.value = ""
            _cloudDate.value = ""
            _isLoading.value = false
        }
        catch (e: Exception){
            _messageManager.value = MessageManager(false)
            // todo gestionar error
        }

    }


    // MÉTODOS
    /**
     * Cambia el valor de la notación en las preferencias de usuario
     * @param value: el nuevo valor, boolean
     */
    fun setNotationValue(value: Boolean) {
        _latinNotes.value = value
        viewModelScope.launch(Dispatchers.IO) {
            prefRepo.setNotationPreference(value)
        }
    }

    /**
     * Deslogea al usuario de firebase
     */
    fun logOut() {
        _auth.signOut()
        _loggedIn.value = false
    }

    /**
     * Se encarga de iniciar
     */
    suspend fun uploadData(): Boolean {
        var success = false
        try {
            _isLoading.value = true
            success = createBackupUseCase.call(Unit)
            _isLoading.value = false
            if(!success){
                _messageManager.value = MessageManager(false)
            }
        } catch (e: Exception) {
            _messageManager.value = MessageManager(false)
        }
        return success
    }

    /**
     * Metodo que se encarga de descargar una copia de seguirada ycargarla en el dispositivo
     * @return boolean indicando si la operación ha sido exitosa
     */
    suspend fun downloadData(): Boolean {
        var success = false
        try {
            _isLoading.value = true
            success = downloadBackupUseCase.call(Unit)
            _isLoading.value = false
            if(!success) _messageManager.value = MessageManager(false)
        } catch (e: Exception) {
            _messageManager.value = MessageManager(false)
            return false
        }
        return success
    }

    suspend fun deleteData(): Boolean{
        try {
            _isLoading.value = true
            val value = deleteCloudDataUseCase.call(Unit)
            _isLoading.value = false
            return value
        } catch (e: Exception) {
            // todo gestionar esto
            Log.d("EL ERROR", e.toString())
            return false
        }
    }

    /**
     * Metodo que llama a DeleteAccountUseCase para borrar la cuenta del usuario
     */
    suspend fun deleteAccount(password: String): Boolean{
        try {
            _isLoading.value = true
            val value = deleteAccountUseCase.call(password)
            _isLoading.value = false
            return value
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
            _messageManager.value = MessageManager(false, "Wrong password")
            _isLoading.value = false
            return false
        }
        catch (e: Exception) {
            _messageManager.value = MessageManager(false)
            return false
        }
    }
}