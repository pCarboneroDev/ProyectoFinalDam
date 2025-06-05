package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

interface UserPreferencesRepository {
    suspend fun getNotationPreference(): Boolean
    suspend fun setNotationPreference(value: Boolean)

    suspend fun setLastModificationDate(value: String)
    suspend fun getLastModificationDate(): String
}