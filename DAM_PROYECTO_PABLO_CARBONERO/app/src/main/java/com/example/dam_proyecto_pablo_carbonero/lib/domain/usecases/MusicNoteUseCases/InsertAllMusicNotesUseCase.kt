package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository

class InsertAllMusicNotesUseCase(
    private val repo: MusicNoteRepository
) : UseCase<List<MusicNote>, List<Long>> {
    /**
     * Usecase que inserta una lista de notas musicales en la bbdd
     * @param param lsita de notas que se van a insertar
     * @return las lista de notas insertada
     */
    override suspend fun call(param: List<MusicNote>): List<Long> {
        return repo.insertAllMusicNotes(param)
    }
}
