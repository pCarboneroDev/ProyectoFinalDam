package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.UserPreferencesRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.features.login.views.LoginView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val prefRepo: UserPreferencesRepositoryImpl
) : ViewModel() {
    private val _auth: FirebaseAuth = Firebase.auth

    private val _latinNotes = MutableStateFlow<Boolean>(false)
    val latinNotes: StateFlow<Boolean> = _latinNotes

    private val _loggedIn = MutableStateFlow<Boolean>(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn


    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        viewModelScope.launch(Dispatchers.Main){
            loadViewmodel()
            _isLoading.value = false
        }
    }

    suspend fun loadViewmodel() {
        _latinNotes.value = prefRepo.getNotationPreference()

        var user = _auth.currentUser

        Log.d("USER LOGGEDIN: ", "${user != null}")

        if (user != null) {
            _loggedIn.value = true
        }
    }


    // MÉTODOS
    fun setNotationValue(value: Boolean) {
        _latinNotes.value = value
        viewModelScope.launch(Dispatchers.IO) {
            prefRepo.setNotationPreference(value)
        }
    }


    fun logOut() {
        _auth.signOut()
        _loggedIn.value = false
    }
}