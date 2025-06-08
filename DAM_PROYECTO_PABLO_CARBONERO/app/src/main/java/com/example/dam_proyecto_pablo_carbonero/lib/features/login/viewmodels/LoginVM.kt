package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SignInWithEmailAndPasswordUseCase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase
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


    fun setEmail(value:String){
        _email.value = value
    }

    fun setPassword(value:String){
        _password.value = value
    }


    suspend fun signInWithEmailAndPassword(): Pair<Boolean, String>{
        _loading.value = true
        var loginSuccesful = true
        var message = ""

        try{
            if(isFormValid()){
                val user = signInWithEmailAndPasswordUseCase.call(UserParams(_email.value, _password.value))
                if (user == null) loginSuccesful = false
            }
            else{
                loginSuccesful = false
            }
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
            message = "Wrong email or password"
            loginSuccesful = false
        }
        catch (e: Exception){
            message = "Unexpected error. Try again later"
            loginSuccesful = false
        }
        _loading.value = false
        return Pair(loginSuccesful, message)
    }

    private fun isEmailValid(): Boolean{
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return (_email.value.isNotEmpty() && _email.value.matches(emailRegex.toRegex()))
    }
    private fun isPasswordValid(): Boolean{
        return  (_password.value.isNotEmpty() && _password.value.length >= 6)
    }

    private fun isFormValid(): Boolean{
        _wrongEmail.value = !isEmailValid()
        _wrongPassword.value = !isPasswordValid()
        return (!_wrongEmail.value && !_wrongPassword.value)
    }

}