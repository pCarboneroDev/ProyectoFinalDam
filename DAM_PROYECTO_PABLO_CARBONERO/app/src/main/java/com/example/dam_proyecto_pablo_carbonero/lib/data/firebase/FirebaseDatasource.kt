package com.example.dam_proyecto_pablo_carbonero.lib.data.firebase

import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.suspendCoroutine

class FirebaseDatasource {
    private val _db = FirebaseFirestore.getInstance()
    private val _auth = FirebaseAuth.getInstance()


    suspend fun getCurrentUser(): FirebaseUser? {
        return try {
            _auth.currentUser
        } catch (e: Exception) {
            throw e;
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null

        try {
            _auth.createUserWithEmailAndPassword(email, password).await()
            user = getCurrentUser()
        } catch (e: Exception) {
            throw e;
        }

        return user
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null

        try {
            _auth.signInWithEmailAndPassword(email, password).await()
            user = getCurrentUser()
        } catch (e: Exception) {
            throw e;
        }
        return user
    }

    suspend fun getCurrentDate(): String {
        try {
            val user = getCurrentUser()

            if (user == null) return ""

            val doc = _db.collection("backups")
                .document(user.uid)
                .get().await()

            val date = doc["lastModificationDate"] as Timestamp

            val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

            return formatter.format(date.toDate())
        } catch (e: Exception) {
            return "No data changes registered"
        }
        return ""
    }

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        var success = true
        try {

            _auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        success = false
                    }
                }

        } catch (e: Exception) {
            throw e;
        }
        return success
    }

    suspend fun deleteCloudData(): Boolean {
        var success = false
        try {
            val user = getCurrentUser()

            if (user != null) {
                _db.collection("backups").document(user.uid).delete().await()
                success = true
            }
        } catch (e: Exception) {
            throw e
        }

        return success
    }

    /**
     * Metodo que se encarga de borrar la cuenta del usuario de firebase
     * @return true si ha sido eliminada con éxito, false si no se ha podido borrar
     */
    suspend fun deleteAccount(password: String): Boolean {
        var success = false

        val user = _auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email.toString(), password)
            user.reauthenticate(credential).await()
            user.delete().await()
            success = true
        }

        return success
    }
}