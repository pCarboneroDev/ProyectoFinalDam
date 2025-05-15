package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningmusicnoteusecase

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository

class GetNotesIdFromTuningIdUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<Long, List<Long>> {
    override suspend fun call(param: Long): List<Long> {
        return repo.getNotesIdFromTuningId(param)
    }
}
