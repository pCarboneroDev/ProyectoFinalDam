package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

/**
 * Interfaz que define todas las posibles acciones que se pueden realizar con las preferencias de usuario
 */
interface UserPreferencesRepository {
    suspend fun getNotationPreference(): Boolean
    suspend fun setNotationPreference(value: Boolean)

    suspend fun setLastModificationDate(value: String)
    suspend fun getLastModificationDate(): String
}