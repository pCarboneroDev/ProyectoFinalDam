package com.example.dam_proyecto_pablo_carbonero.lib.extensions

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote

/**
 * Función de extensión que convierte un map de firebase en un objeto de MusicNote válido para la bbdd
 * @return Objeto de MusicNote
 */
fun Map<String, Any>.toMusicNote(): MusicNote{
    return MusicNote(
        id = (this["id"] as? Long) ?: 0L,
        latinName = this["latinName"] as? String ?: "",
        englishName = this["englishName"] as? String ?: "",
        latinNotation = this["latinNotation"] as? String ?: "",
        englishNotation = this["englishNotation"] as? String ?: "",
        octave = (this["octave"] as? Long)?.toInt() ?: 0,
        maxHz = (this["maxHz"] as? Double) ?: 0.0,
        minHz = (this["minHz"] as? Double) ?: 0.0,
        sharpIndicator = this["sharpIndicator"] as? String,
        alternativeName = this["alternativeName"] as? String
    )
}

/**
 * Función de extensión que convierte un map de firebase en un objeto de Tuning válido para la bbdd
 * @return Objeto de Tuning
 */
fun Map<String, Any>.toTuning(): Tuning{
    return Tuning(
        id = (this["id"] as? Long) ?: 0L,
        name = (this["name"] as? String) ?: "",
        favourite = (this["favourite"] as? Boolean) ?: false
    )
}

/**
 * Función de extensión que convierte un map de firebase en un objeto de TuningMusicNote válido para la bbdd
 * @return Objeto de TuningMusicNote
 */
fun Map<String, Any>.toTuningMusicNote(): TuningMusicNote{
    return TuningMusicNote(
        id = (this["id"] as? Long) ?: 0L,
        tuningId = (this["tuningId"] as? Long) ?: 0L,
        noteId = (this["noteId"] as? Long) ?: 0L,
    )
}

/**
 * Función de extensión que convierte un map de firebase en un objeto de Song válido para la bbdd
 * @return Objeto de Song
 */
fun Map<String, Any>.toSong(): Song{
    return Song(
        id = (this["id"] as? Long) ?: 0L,
        name = (this["name"] as? String) ?: "",
        bandName = (this["bandName"] as? String) ?: "",
        tuningId = (this["tuningId"] as? Long) ?: 0L,
        bpm = (this["bpm"] as? String) ?: "",
        tabs = (this["tabs"] as? String) ?: "",
    )
}