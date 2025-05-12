package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningmusicnoteusecase

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository

data class UpdateTuningNoteIdParams(
    val tuningId: Long,
    val noteId: Long
)
class UpdateTuningNoteIdUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<UpdateTuningNoteIdParams, Unit> {
    override suspend fun call(param: UpdateTuningNoteIdParams) {
        return repo.updateTuningNoteId(param.tuningId, param.noteId)
    }
}
