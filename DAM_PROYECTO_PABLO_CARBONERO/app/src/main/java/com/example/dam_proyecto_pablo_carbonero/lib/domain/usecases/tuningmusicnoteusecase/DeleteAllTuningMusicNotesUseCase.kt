package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningmusicnoteusecase

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository

class DeleteAllTuningMusicNotesUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<Unit, Int> {
    override suspend fun call(param: Unit): Int {
        return repo.deleteAllTuningMusicNote()
    }
}
