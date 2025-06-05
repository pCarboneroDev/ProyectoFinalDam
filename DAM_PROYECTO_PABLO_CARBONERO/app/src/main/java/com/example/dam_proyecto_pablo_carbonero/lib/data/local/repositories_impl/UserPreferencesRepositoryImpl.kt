package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.datastore.PreferenceKeys
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
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

    override suspend fun setLastModificationDate(value: String) {
        datastore.edit { pref ->
            pref[PreferenceKeys.lastModificationDate] = value
        }
    }

    override suspend fun getLastModificationDate(): String {
        val lastModificationDate: Flow<String> = datastore.data.catch { e ->
            if(e is IOException){
                emit(emptyPreferences())
            }
        }.map { prefs ->
            prefs[PreferenceKeys.lastModificationDate] ?: ""
        }
        return lastModificationDate.firstOrNull() ?: ""
    }
}