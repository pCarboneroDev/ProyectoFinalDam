package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote

interface TuningMusicNoteRepository {
    suspend fun getAllTuningMusicNote():List<TuningMusicNote>

    suspend fun insertTuningMusicNote(notaAfinacion: TuningMusicNote): Long

    suspend fun getNotesIdFromTuningId(tuningId: Long): List<Long>

    suspend fun deleteAllTuningMusicNote(): Int

    suspend fun deleteNoteByTuningId(tuningId: Long): Int

    suspend fun updateTuningNoteId(tuningId: Long, noteId: Long)
}