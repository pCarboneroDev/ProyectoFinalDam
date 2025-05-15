package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote

/**
 * El motivo por el que se crean repositories los cuales en principio son iguales que las funciones dao
 * es respetar la arquitectura "Clean architecture". Si en el futuro se quiere cambiar el tipo de fuente de
 * datos es más fácil
 *
 * El objetivo de
 */
interface MusicNoteRepository {
    suspend fun getAllNotes():List<MusicNote>

    suspend fun insertMusicNote(note: MusicNote): Long

    suspend fun insertAllMusicNotes(note: List<MusicNote>): List<Long>

    suspend fun deleteAllMusicNotes(): Int

    suspend fun getMusicNoteById(noteId: Long): MusicNote
}