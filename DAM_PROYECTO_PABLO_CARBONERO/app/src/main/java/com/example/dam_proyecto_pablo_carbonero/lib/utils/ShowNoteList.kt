package com.example.dam_proyecto_pablo_carbonero.lib.utils

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote

/**
 * función para obtener las notas como un string
 */
fun NoteList(list: List<MusicNote>, latin: Boolean): String{
    var listString = ""
    list.forEach { note ->
        listString += if (latin) "${note.latinName} " else "${note.englishName} "
    }
    return listString
}