package com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.repository_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.FirebaseDatasource
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

/**
 * Implementación de las posibles acciones de Firebase
 */
class FirebaseRepositoryImpl @Inject constructor(
    private val firebase: FirebaseDatasource
): FirebaseRepository {

    /**
     * Accede a FirebaseDatasource para obtener el usuario actual
     */
    override suspend fun getCurrentUser(): FirebaseUser? {
        return firebase.getCurrentUser()
    }

    /**
     * Accede a FirebaseDatasource para crear una cuenta
     * @param email
     * @param password
     * @return FirebaseUser Usuario de Firebase
     */
    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseUser? {
        return firebase.createUserWithEmailAndPassword(email, password)
    }

    /**
     * Accede a FirebaseDatasource para logear a un usuario
     * @param email
     * @param password
     * @return FirebaseUser: un usuario de Firbase
     */
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseUser? {
        return firebase.signInWithEmailAndPassword(email, password)
    }

    /**
     * Accede a FirebaseDatasource para obtener la última fecha de modificación si la hay
     * @return String con la fecha o con un texto indicando que no hay
     */
    override suspend fun getCurrentDate(): String {
        return firebase.getCurrentDate()
    }

    /**
     * Accede a FirebaseDatasource que envía a una dirección de correo un email para restablecer la contrseña
     * @param email
     * @return boolean indicando si se ha podido completar la acción
     */
    override suspend fun sendPasswordResetEmail(email: String): Boolean {
        return firebase.sendPasswordResetEmail(email)
    }

    /**
     * Accede a FirebaseDatasource para borrar los datos en Firebase de un usuario
     */
    override suspend fun deleteCloudData(): Boolean {
        return firebase.deleteCloudData()
    }

    /**
     * Accede a FirebaseDatasource para borrar la cuenta de un usuario
     * @param password
     * @return boolean indicando si se ha podido borrar
     */
    override suspend fun deleteAccount(password: String): Boolean {
        return firebase.deleteAccount(password)
    }

    /**
     * Accede a FirebaseDatasource para crear una copia de los datos del dispositivo ne Firebase
     * @param map los datos mapeados para enviarlos a firbase
     * @return boolean indicando si se ha creado
     */
    override suspend fun createBackup(map: Map<String, Any>): Boolean {
        return firebase.createBackUp(map)
    }

    /**
     * Accede a FirebaseDatasource para descargar la copia de los datos en la nube
     * @return DocumentSnapShot? el documento de firebase descargado, null si no hay
     */
    override suspend fun downloadBackup(): DocumentSnapshot? {
        return firebase.downloadBackup()
    }
}