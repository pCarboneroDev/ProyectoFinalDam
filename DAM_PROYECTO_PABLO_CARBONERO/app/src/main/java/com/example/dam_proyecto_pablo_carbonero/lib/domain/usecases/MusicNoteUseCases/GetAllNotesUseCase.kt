package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    val repo: MusicNoteRepository
): UseCase<Unit, List<MusicNote>>{
    /**
     * Usecase que devuelve una lista con todas las notas musicales de la bbdd
     * @return lista de MusicNote
     */
    override suspend fun call(param: Unit): List<MusicNote> {
        return repo.getAllNotes()
    }
}