package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Interfaz que define todas las posibles acciones que se pueden realizar con firebase
 */
interface FirebaseRepository {
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser?
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser?
    suspend fun getCurrentDate(): String
    suspend fun sendPasswordResetEmail(email: String): Boolean
    suspend fun deleteCloudData(): Boolean
    suspend fun deleteAccount(password: String): Boolean
    suspend fun createBackup(map: Map<String, Any>): Boolean
    suspend fun downloadBackup(): DocumentSnapshot?
}