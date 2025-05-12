package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository


class GetAllNotesUseCase(
    val repo: MusicNoteRepository
): UseCase<Unit, List<MusicNote>>{
    override suspend fun call(param: Unit): List<MusicNote> {
        return repo.getAllNotes()
    }
}