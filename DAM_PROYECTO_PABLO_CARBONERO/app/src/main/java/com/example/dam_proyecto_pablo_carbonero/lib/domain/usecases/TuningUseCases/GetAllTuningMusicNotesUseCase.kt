package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository

class GetAllTuningMusicNotesUseCase(
    private val repo: TuningMusicNoteRepository
) : UseCase<Unit, List<TuningMusicNote>> {
    override suspend fun call(param: Unit): List<TuningMusicNote> {
        return repo.getAllTuningMusicNote()
    }
}
