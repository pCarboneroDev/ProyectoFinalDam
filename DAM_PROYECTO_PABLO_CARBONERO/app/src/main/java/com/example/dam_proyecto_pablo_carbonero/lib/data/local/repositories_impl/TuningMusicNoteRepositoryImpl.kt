package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import javax.inject.Inject

class TuningMusicNoteRepositoryImpl @Inject constructor(
    val tuningMusicNoteDao: TuningMusicNoteDao
): TuningMusicNoteRepository {
    override suspend fun getAllTuningMusicNote(): List<TuningMusicNote> {
        return tuningMusicNoteDao.getAllTuningMusicNote()
    }

    override suspend fun insertTuningMusicNote(notaAfinacion: TuningMusicNote): Long {
        return tuningMusicNoteDao.insertTuningMusicNote(notaAfinacion)
    }

    override suspend fun getNotesIdFromTuningId(tuningId: Long): List<Long> {
        return tuningMusicNoteDao.getNotesIdFromTuningId(tuningId)
    }

    override suspend fun deleteAllTuningMusicNote(): Int {
        return tuningMusicNoteDao.deleteAllTuningMusicNote()
    }

    override suspend fun deleteNoteByTuningId(tuningId: Long): Int {
        return tuningMusicNoteDao.deleteNoteByTuningId(tuningId)
    }

    override suspend fun updateTuningNoteId(tuningId: Long, noteId: Long) {
        return tuningMusicNoteDao.updateTuningNoteId(tuningId, noteId)
    }

    override suspend fun insertAllTuningMusicNote(list: List<TuningMusicNote>): List<Long> {
        return tuningMusicNoteDao.insertAllTuningMusicNote(list)
    }
}