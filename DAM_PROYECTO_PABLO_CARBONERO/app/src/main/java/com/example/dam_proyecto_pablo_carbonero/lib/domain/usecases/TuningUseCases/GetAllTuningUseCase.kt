package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository

class GetAllTuningUseCase(
    private val repo: TuningRepository
) : UseCase<Unit, List<Tuning>> {
    override suspend fun call(param: Unit): List<Tuning> {
        return repo.getAllTunings()
    }
}
