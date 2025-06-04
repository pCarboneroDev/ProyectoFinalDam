package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SignInWithEmailAndPasswordUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase
): ViewModel() {
    private val _auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password


    fun setEmail(value:String){
        _email.value = value
    }

    fun setPassword(value:String){
        _password.value = value
    }


    suspend fun signInWithEmailAndPassword(): Boolean{
        //_loading.value = true
        var loginSuccesful = true

        try{
            val user = signInWithEmailAndPasswordUseCase.call(UserParams(_email.value, _password.value))
            if (user == null) loginSuccesful = false
        }
        catch (e: Exception){
            loginSuccesful = false
        }

        return loginSuccesful
    }

}