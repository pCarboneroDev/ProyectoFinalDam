package com.example.dam_proyecto_pablo_carbonero.lib.data.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
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


    /**
     * Metodo que hace una consulta con firebase y obtiene lso datos del usuario que tiene la sesión inicada
     */
    suspend fun getCurrentUser(): FirebaseUser? {
        return try {
            _auth.currentUser
        } catch (e: Exception) {
            throw e;
        }
    }

    /**
     * Metodo que se encarga de crear un nuevo usuario en firebase
     * @param email email del nuevo usuario
     * @param password contraseña del nuevo usuario
     * @return el nuevo usuario registrado, null si hay algún fallo
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null
        _auth.createUserWithEmailAndPassword(email, password).await()
        user = getCurrentUser()
        return user
    }

    /**
     * Metodo que se encarga de logear a un usuario registrado
     * @param email email del usuario
     * @param password contraseña del usuario
     * @return el usuario
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        var user: FirebaseUser? = null
        _auth.signInWithEmailAndPassword(email, password).await()
        user = getCurrentUser()
        return user
    }

    /**
     * Metodo que se encarga de subir a firebase una copia de la bbdd local serializada
     * @param data los datos serialziados
     * @return si ha sido un éxito o no
     */
    suspend fun createBackUp(data: Map<String, Any>): Boolean{
        val success = false
        val user = getCurrentUser()

        if (user != null){
            _db.collection("backups")
                .document(user.uid)
                .set(data).await()
            return true
        }
        return success
    }

    /**
     * Metodo que descarga los datos guardados en la bbdd de firebase
     */
    suspend fun downloadBackup(): DocumentSnapshot? {
        val user = getCurrentUser()
        var doc: DocumentSnapshot? = null
        if(user != null)
            doc = _db.collection("backups")
                .document(user.uid)
                .get().await()

        return doc
    }

    /**
     * Metodo que comprueba la última fecha de modificación registrada en firebase
     * @return la fecha en formato string o mensaje informando que no hay ningún dato
     */
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
    }

    /**
     * Metodo que se encarga de enviar a una dirección de correo un enlace para reestablecer su contraseña
     * @param la dirección a la que se enviará el correo
     * @return boolean indicando si ha podido enviarse o no
     */
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

    /**
     * Metodo que borra los datos guardados de un usuario en la bbdd de firebase
     */
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