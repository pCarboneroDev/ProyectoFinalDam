package com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "MusicNotes")
data class MusicNote(
    @PrimaryKey
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
    constructor() : this(
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
        var name = latinNotation
        if (sharpIndicator != null) {
            name += "$sharpIndicator $octave"
        }
        else{
            name += octave
        }
        return name
    }

    private fun createEnglishName(): String {
        var name = englishNotation
        if (sharpIndicator != null) {
            name += "$sharpIndicator $octave"
        }
        else{
            name += octave
        }
        return name
    }

    override fun toString(): String {
        var string = ""
        string += "Nombre Latino: $latinNotation\n"
        string += "Nombre Anglosajón: $englishNotation\n"
        string += "Octava: $octave\n"
        string += "Frecuencia Mínima: $minHz Hz\n"
        string += "Frecuencia Máxima: $maxHz Hz\n"
        string += "Semitono Sostenido: ${sharpIndicator ?: "Ninguno"}\n"
        string += "Semitono Bemol: ${alternativeName ?: "Ninguno"}\n"
        return string
    }

    override fun compareTo(other: MusicNote): Int {
        return (this.maxHz - other.maxHz).toInt()
    }
}
