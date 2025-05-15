package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import javax.inject.Inject

class MusicNoteRepositoryImpl @Inject constructor(
    val musicNoteDao: MusicNoteDao
): MusicNoteRepository {

    override suspend fun getAllNotes(): List<MusicNote> {
        return musicNoteDao.getAllNotes()
    }

    override suspend fun insertMusicNote(note: MusicNote): Long {
        return musicNoteDao.insertMusicNote(note)
    }

    override suspend fun insertAllMusicNotes(notes: List<MusicNote>): List<Long> {
        return musicNoteDao.insertAllMusicNotes(notes)
    }

    override suspend fun deleteAllMusicNotes(): Int {
        return musicNoteDao.deleteAllMusicNotes()
    }

    override suspend fun getMusicNoteById(noteId: Long): MusicNote {
        return musicNoteDao.getMusicNoteById(noteId)
    }
}