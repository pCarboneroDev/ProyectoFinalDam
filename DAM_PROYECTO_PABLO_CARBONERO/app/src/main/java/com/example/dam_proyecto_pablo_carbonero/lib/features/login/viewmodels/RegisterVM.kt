package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateUserWithEmailAndPasswordUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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
    private val _auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow<String>("")
    val confirmPassword: StateFlow<String> = _confirmPassword


    fun setEmail(value:String){
        _email.value = value
    }

    fun setpassword(value:String){
        _password.value = value
    }

    fun _confirmPassword(value:String){
        _password.value = value
    }


    suspend fun createUserWithEmailAndPassword(): Boolean{
        var registerSuccesful = true

        try{
            val user = createUserWithEmailAndPasswordUseCase.call(UserParams(_email.value, _password.value))
            if (user == null) registerSuccesful = false
        }
        catch (e: Exception){
            registerSuccesful = false
        }

        return registerSuccesful
    }


    private fun _isFormValid(): Boolean{
        return (_email.value.isNotEmpty())
                && (_password.value.isNotEmpty() && _password.value.length >= 6)
    }

}