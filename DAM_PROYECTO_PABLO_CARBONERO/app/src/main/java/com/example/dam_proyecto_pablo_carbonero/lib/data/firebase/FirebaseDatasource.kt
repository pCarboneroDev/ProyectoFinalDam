package com.example.dam_proyecto_pablo_carbonero.lib.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseDatasource {
    private val _db = FirebaseFirestore.getInstance()
    private val _auth = FirebaseAuth.getInstance()


    suspend fun getCurrentUser(): FirebaseUser? {
        return try{
            _auth.currentUser
        }catch (e: Exception){
            throw e;
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null

        try{
            _auth.createUserWithEmailAndPassword(email, password).await()
            user = getCurrentUser()
        }
        catch (e: Exception){
            throw e;
        }

        return user
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null

        try{
            _auth.signInWithEmailAndPassword(email, password).await()
            user = getCurrentUser()
        }
        catch (e: Exception){
            throw e;
        }
        return user
    }
}