package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningmusicnoteusecase

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository

class DeleteNoteByTuningIdUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<Long, Int> {
    override suspend fun call(param: Long): Int {
        return repo.deleteNoteByTuningId(param)
    }
}
