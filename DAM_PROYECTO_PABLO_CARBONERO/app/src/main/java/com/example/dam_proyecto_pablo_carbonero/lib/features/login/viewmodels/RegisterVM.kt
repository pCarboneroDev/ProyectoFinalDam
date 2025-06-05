package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateUserWithEmailAndPasswordUseCase
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

    //private val _confirmPassword = MutableStateFlow<String>("")
    //val confirmPassword: StateFlow<String> = _confirmPassword


    fun setEmail(value:String){
        _email.value = value
    }

    fun setpassword(value:String){
        _password.value = value
    }

    /*fun _confirmPassword(value:String){
        _password.value = value
    }*/


    suspend fun createUserWithEmailAndPassword(): Boolean{
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
            Log.d("EX", e.toString())
            registerSuccesful = false
        }
        catch (e: Exception){
            Log.d("EX", e.toString())
            registerSuccesful = false
        }
        _loading.value = false
        return registerSuccesful
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