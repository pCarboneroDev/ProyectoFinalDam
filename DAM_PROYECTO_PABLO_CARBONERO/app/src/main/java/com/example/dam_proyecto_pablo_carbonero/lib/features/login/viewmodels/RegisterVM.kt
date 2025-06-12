package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateUserWithEmailAndPasswordUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.MessageManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase
): ViewModel() {

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _wrongPassword = MutableStateFlow<Boolean>(false)
    val wrongPassword: StateFlow<Boolean> = _wrongPassword

    private val _wrongEmail = MutableStateFlow<Boolean>(false)
    val wrongEmail: StateFlow<Boolean> = _wrongEmail

    private val _messageManager = MutableStateFlow<MessageManager>(MessageManager(true))
    val messageManager: StateFlow<MessageManager> = _messageManager


    fun setEmail(value:String){
        _email.value = value
    }

    fun setpassword(value:String){
        _password.value = value
    }


    /**
     * Llama al caso de uso para realizar el registro del usuario en firebase
     * @return boolean indicando si ha salido bien la acción
     */
    suspend fun createUserWithEmailAndPassword(): Boolean {
        _loading.value = true
        var registerSuccesful = true

        try{
            if(isFormValid()){
                val user = createUserWithEmailAndPasswordUseCase.call(UserParams(_email.value, _password.value))
                if (user == null) registerSuccesful = false
            }
            else{
                registerSuccesful = false
            }
        }
        catch (e: FirebaseAuthUserCollisionException){
            _messageManager.value = MessageManager(false, "Email already in use")
            registerSuccesful = false
        }
        catch(e: FirebaseAuthInvalidCredentialsException){
            //_messageManager.value = MessageManager(false, e.message.toString())
            _wrongEmail.value = true
            registerSuccesful = false
        }
        catch (e: Exception){
            _messageManager.value = MessageManager(false)
            registerSuccesful = false
        }
        _loading.value = false
        return registerSuccesful
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
     * Metodo que comprueba que la contraseña introducida
     * @return boolean indicando si es correcto o no
     */
    private fun isPasswordValid(): Boolean{
        return  (_password.value.isNotEmpty() && _password.value.length >= 6)
    }

    /**
     * Metodo que comprueba que todos los campos están correctamente completos
     * @return boolean indicando si es correcto o no
     */
    private fun isFormValid(): Boolean{
        _wrongEmail.value = !isEmailValid()
        _wrongPassword.value = !isPasswordValid()
        return (!_wrongEmail.value && !_wrongPassword.value)
    }

    /**
     * Resetea el valor del message manager
     */
    fun resetMessageManager(){
        _messageManager.value = MessageManager(true)
    }

}