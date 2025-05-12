package com.example.dam_proyecto_pablo_carbonero.lib.repositories

interface UserPreferencesRepository {
    suspend fun getNotationPreference(): Boolean

    suspend fun setNotationPreference(value: Boolean)
}