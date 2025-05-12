package com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "MusicNotes")
data class MusicNote(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var latinName: String = "",
    var englishName: String = "",
    val latinNotation: String,
    val englishNotation: String,
    val octave: Int,
    val maxHz: Double,
    val minHz: Double,
    val sharpIndicator: String?,
    val alternativeName: String?
): Comparable<MusicNote> {

    @Ignore
    var autoInitialize: Boolean = true

    init {
        if (autoInitialize) {
            latinName = createLatinName()
            englishName = createEnglishName()
        }
    }

    // CONSTRUCTOR SIN PARAMETROS PARA CREAR OBJETOS SIN INICIALIZAR
    @Ignore
    constructor() : this( // ERROR AQUÍ
        id = 0,
        latinName = "",
        englishName = "",
        latinNotation = "",
        englishNotation = "",
        octave = 0,
        maxHz = 0.0,
        minHz = 0.0,
        sharpIndicator = null,
        alternativeName = null
    ){
        autoInitialize = false
    }

    private fun createLatinName(): String {
        var nombre = latinNotation + "$octave"
        if (sharpIndicator != null) {
            nombre += sharpIndicator
        }
        return nombre
    }

    private fun createEnglishName(): String {
        var nombre = englishNotation + "$octave"
        if (sharpIndicator != null) {
            nombre += sharpIndicator
        }
        return nombre
    }

    override fun toString(): String {
        var cadena = ""
        cadena += "Nombre Latino: $latinNotation\n"
        cadena += "Nombre Anglosajón: $englishNotation\n"
        cadena += "Octava: $octave\n"
        cadena += "Frecuencia Mínima: $minHz Hz\n"
        cadena += "Frecuencia Máxima: $maxHz Hz\n"
        cadena += "Semitono Sostenido: ${sharpIndicator ?: "Ninguno"}\n"
        cadena += "Semitono Bemol: ${alternativeName ?: "Ninguno"}\n"
        return cadena
    }

    override fun compareTo(other: MusicNote): Int {
        return (this.maxHz - other.maxHz).toInt()
    }
}
