package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import javax.inject.Inject

class MusicNoteRepositoryImpl @Inject constructor(
    val musicNoteDao: MusicNoteDao
): MusicNoteRepository {

    /**
     * Accede a musicNoteDao para obtener todas las notas de la bbdd
     * @return lista de MusicNote
     */
    override suspend fun getAllNotes(): List<MusicNote> {
        return musicNoteDao.getAllNotes()
    }

    /**
     * Accede a musicNoteDao para insertar una nota en la BBDD
     * @param note objeto que se quiere insertar
     * @return id del objeto insertado, 0 si no se ha completado
     */
    override suspend fun insertMusicNote(note: MusicNote): Long {
        return musicNoteDao.insertMusicNote(note)
    }

    /**
     * Accede a musicNoteDao para insertar una lista de notas
     * @param notes lista de notas que se van a insertar
     * @return lista de los ids de las ntoas insertadas
     */
    override suspend fun insertAllMusicNotes(notes: List<MusicNote>): List<Long> {
        return musicNoteDao.insertAllMusicNotes(notes)
    }

    /**
     * Accede a musicNoteDao para borrar todas las notas de la bbdd
     * @return nº de filas afectadas
     */
    override suspend fun deleteAllMusicNotes(): Int {
        return musicNoteDao.deleteAllMusicNotes()
    }

    /**
     * Accede a musicNoteDao para obtener una nota por id
     * @param noteId id de la nota buscada
     * @return objeto de MusicNote bscada
     */
    override suspend fun getMusicNoteById(noteId: Long): MusicNote {
        return musicNoteDao.getMusicNoteById(noteId)
    }

    /**
     * Accede a musicNoteDao para obtener una lista de notas determinada
     */
    override suspend fun getAmountNotes(
        number: Int,
        lastIndex: Long
    ): List<MusicNote> {
        return musicNoteDao.getAmountNotes(number, lastIndex);
    }
}