package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.google.firebase.auth.FirebaseUser

interface FirebaseRepository {
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser?
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser?
    suspend fun getCurrentDate(): String
    suspend fun sendPasswordResetEmail(email: String): Boolean
}