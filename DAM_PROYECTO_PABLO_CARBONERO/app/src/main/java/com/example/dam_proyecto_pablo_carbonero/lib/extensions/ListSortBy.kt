package com.example.dam_proyecto_pablo_carbonero.lib.extensions


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