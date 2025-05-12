package com.example.dam_proyecto_pablo_carbonero.lib.repositories.repositories_impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.datastore.PreferenceKeys
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    //@ApplicationContext private val context: Context
    private val datastore: DataStore<Preferences>
): UserPreferencesRepository {
    //private val Context.dataStore by preferencesDataStore(name = "user_prefs") // this line is the one creating a datastore everytime?

    override suspend
    fun getNotationPreference(): Boolean {
        val englishNotes: Flow<Boolean> = datastore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[PreferenceKeys.latinNotes] ?: false
        }
        return englishNotes.firstOrNull() ?: false
    }

    override suspend fun setNotationPreference(value: Boolean) {
        datastore.edit {  pref ->
            pref[PreferenceKeys.latinNotes] = value
        }
    }
}