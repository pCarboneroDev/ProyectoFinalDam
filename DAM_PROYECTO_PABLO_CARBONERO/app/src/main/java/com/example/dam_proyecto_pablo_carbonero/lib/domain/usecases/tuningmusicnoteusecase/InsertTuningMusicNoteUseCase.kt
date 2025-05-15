package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.tuningmusicnoteusecase

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository

class InsertTuningMusicNoteUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<TuningMusicNote, Long> {
    override suspend fun call(param: TuningMusicNote): Long {
        return repo.insertTuningMusicNote(param)
    }
}
