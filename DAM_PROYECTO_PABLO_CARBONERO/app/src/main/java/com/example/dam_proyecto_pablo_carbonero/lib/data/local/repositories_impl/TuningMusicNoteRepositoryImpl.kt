package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import javax.inject.Inject

class TuningMusicNoteRepositoryImpl @Inject constructor(
    val tuningMusicNoteDao: TuningMusicNoteDao
): TuningMusicNoteRepository {

    /**
     * Accede al tuningMusicNoteDao para obtener todas las notas de afinación
     * @return lista de TuningMusicNote
     */
    override suspend fun getAllTuningMusicNote(): List<TuningMusicNote> {
        return tuningMusicNoteDao.getAllTuningMusicNote()
    }

    /**
     * Accede al tuningMusicNoteDao para insertar una nota de afinación
     * @param notaAfinacion objeto de tipo TuningMusicNote
     * @return ID de la nota insertada
     */
    override suspend fun insertTuningMusicNote(notaAfinacion: TuningMusicNote): Long {
        return tuningMusicNoteDao.insertTuningMusicNote(notaAfinacion)
    }

    /**
     * Accede al tuningMusicNoteDao para obtener los IDs de las notas
     * asociadas a una afinación concreta
     * @param tuningId ID de la afinación
     * @return lista de IDs de notas
     */
    override suspend fun getNotesIdFromTuningId(tuningId: Long): List<Long> {
        return tuningMusicNoteDao.getNotesIdFromTuningId(tuningId)
    }

    /**
     * Accede al tuningMusicNoteDao para eliminar todas las notas de afinación
     * @return número de filas afectadas
     */
    override suspend fun deleteAllTuningMusicNote(): Int {
        return tuningMusicNoteDao.deleteAllTuningMusicNote()
    }

    /**
     * Accede al tuningMusicNoteDao para eliminar las notas asociadas a una afinación
     * @param tuningId ID de la afinación
     * @return número de filas afectadas
     */
    override suspend fun deleteNoteByTuningId(tuningId: Long): Int {
        return tuningMusicNoteDao.deleteNoteByTuningId(tuningId)
    }

    /**
     * Accede al tuningMusicNoteDao para actualizar el ID de la nota de una afinación
     * @param tuningId ID de la afinación
     * @param noteId nuevo ID de la nota
     */
    override suspend fun updateTuningNoteId(tuningId: Long, noteId: Long) {
        return tuningMusicNoteDao.updateTuningNoteId(tuningId, noteId)
    }

    /**
     * Accede al tuningMusicNoteDao para insertar una lista de notas de afinación
     * @param list lista de TuningMusicNote
     * @return lista de IDs generados tras la inserción
     */
    override suspend fun insertAllTuningMusicNote(list: List<TuningMusicNote>): List<Long> {
        return tuningMusicNoteDao.insertAllTuningMusicNote(list)
    }
}