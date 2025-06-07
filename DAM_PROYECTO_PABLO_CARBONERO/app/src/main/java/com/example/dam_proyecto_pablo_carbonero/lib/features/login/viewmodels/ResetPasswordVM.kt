package com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SendPasswordResetEmailUseCase
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

    fun setEmail(value:String){
        _email.value = value
    }

    suspend fun sendEmail(){
        try{
            if(isEmailValid()){
                sendPasswordResetEmailUseCase.call(_email.value)
            }
            else{
                _wrongEmail.value = true
            }
        }
        catch (e: Exception){
            Log.d("ERROR", e.toString())
        }
    }

    private fun isEmailValid(): Boolean{
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return (_email.value.isNotEmpty() && _email.value.matches(emailRegex.toRegex()))
    }
}