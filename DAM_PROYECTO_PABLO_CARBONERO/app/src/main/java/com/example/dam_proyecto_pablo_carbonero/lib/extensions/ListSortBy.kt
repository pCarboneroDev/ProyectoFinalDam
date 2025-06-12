package com.example.dam_proyecto_pablo_carbonero.lib.extensions

import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import java.util.Locale
import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending

/**
 * enum que representa las distintas formas de ordenar la lista de canciones
 */
enum class SortOption {
    NAME_ASCENDING,
    NAME_DESCENDING,
    DATE_ASCENDING,
    DATE_DESCENDING,
    BAND_NAME,
    BAND_NAME_DESCENDING
}


fun SortOption.getName(): String {
    return when (this) {
        SortOption.NAME_ASCENDING -> "Name (A-Z)"
        SortOption.NAME_DESCENDING -> "Name (Z-A)"
        SortOption.DATE_ASCENDING -> "Date (new to old)"
        SortOption.DATE_DESCENDING -> "Date (old to new)"
        SortOption.BAND_NAME -> "Band name (A-Z)"
        SortOption.BAND_NAME_DESCENDING -> "Band name (Z-A)"
    }
}