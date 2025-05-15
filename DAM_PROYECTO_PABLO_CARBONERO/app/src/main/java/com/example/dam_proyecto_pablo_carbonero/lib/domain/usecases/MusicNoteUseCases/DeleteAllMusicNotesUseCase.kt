package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository

class DeleteAllMusicNotesUseCase(
    private val repo: MusicNoteRepository
) : UseCase<Unit, Int> {
    override suspend fun call(param: Unit): Int {
        return repo.deleteAllMusicNotes()
    }
}
