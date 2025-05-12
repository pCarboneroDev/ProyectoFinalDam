package com.example.dam_proyecto_pablo_carbonero.lib.extensions

import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.models.SongWithTuning
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

/**
 * Metodo de extensión de listas que de SongWithTuning
 * Este metodo según el tipo de SortOption que le llegue ordenará la lista de una forma
 * u otra gracias al metodo que tiene List sortedBy y sortedByDescending
 */
fun List<SongWithTuning>.sortByOption(sortOption: SortOption): List<SongWithTuning> {
    return when (sortOption) {
        SortOption.NAME_ASCENDING -> sortedBy { it.song.name.lowercase() }
        SortOption.NAME_DESCENDING -> sortedByDescending { it.song.name.lowercase() }
        SortOption.DATE_ASCENDING -> sortedBy { it.song.id }
        SortOption.DATE_DESCENDING -> sortedByDescending { it.song.id }
        SortOption.BAND_NAME -> sortedBy { it.song.bandName.lowercase() }
        SortOption.BAND_NAME_DESCENDING -> sortedByDescending { it.song.bandName.lowercase() }
    }
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