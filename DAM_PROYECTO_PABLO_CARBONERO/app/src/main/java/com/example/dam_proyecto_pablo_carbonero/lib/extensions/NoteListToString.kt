package com.example.dam_proyecto_pablo_carbonero.lib.extensions

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote

/**
 * Función de extensión para devolver las notas de una afinación como un String
 */
fun List<MusicNote>.toString(latinNotes: Boolean): String{
    var listString = ""
    this.forEach { note ->
        listString += if (latinNotes) "${note.latinName} " else "${note.englishName} "
    }
    return listString
}