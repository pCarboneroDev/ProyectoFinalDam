package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SendPasswordResetEmailUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordVM @Inject constructor(
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
): ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _wrongEmail = MutableStateFlow<Boolean>(false)
    val wrongEmail: StateFlow<Boolean> = _wrongEmail

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mailSent = MutableStateFlow<Boolean>(false)
    val mailSent: StateFlow<Boolean> = _mailSent

    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true, ""))
    val messageManager: StateFlow<MessageManager> = _messageManager

    fun setEmail(value:String){
        _email.value = value
    }

    /**
     * Metodo que llama al caso de uso para enviar un correo de cambiar la contraseña
     */
    suspend fun sendEmail(){
        _isLoading.value = true
        try{
            if(isEmailValid()){
                sendPasswordResetEmailUseCase.call(_email.value)
            }
            else{
                _wrongEmail.value = true
            }
        }
        catch (e: Exception){
            _messageManager.value = MessageManager(false)
            _isLoading.value = false
        }
        _isLoading.value = false
        _mailSent.value = true
    }

    /**
     * Metodo que comprueba que el email introducido sea válido
     * @return boolean indicando si es correcto o no
     */
    private fun isEmailValid(): Boolean{
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return (_email.value.isNotEmpty() && _email.value.matches(emailRegex.toRegex()))
    }


    /**
     * Resetea el valor del gestor de toasts
     */
    fun resetMessageManager(){
        _messageManager.value = MessageManager(true)
    }
}