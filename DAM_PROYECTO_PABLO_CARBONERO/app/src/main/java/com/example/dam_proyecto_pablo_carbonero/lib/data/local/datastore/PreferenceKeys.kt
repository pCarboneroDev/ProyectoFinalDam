package com.example.dam_proyecto_pablo_carbonero.lib.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val latinNotes = booleanPreferencesKey("english_notes")
    val lastModificationDate = stringPreferencesKey("lastModificationDate")
}