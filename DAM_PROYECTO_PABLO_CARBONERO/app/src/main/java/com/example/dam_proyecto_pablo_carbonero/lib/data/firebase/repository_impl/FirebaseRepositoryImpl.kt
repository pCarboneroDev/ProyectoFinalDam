package com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.repository_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.FirebaseDatasource
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebase: FirebaseDatasource
): FirebaseRepository {

    override suspend fun getCurrentUser(): FirebaseUser? {
        return firebase.getCurrentUser()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseUser? {
        return firebase.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseUser? {
        return firebase.signInWithEmailAndPassword(email, password)
    }

    override suspend fun getCurrentDate(): String {
        return firebase.getCurrentDate()
    }
}