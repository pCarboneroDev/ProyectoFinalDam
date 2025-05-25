package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class GetAmountTuningsUseCase @Inject constructor(
    private val getAllTuningWithNotesUseCase: GetAllTuningWithNotesUseCase
): UseCase<Int, List<TuningWithNotesModel>> {
    override suspend fun call(param: Int): List<TuningWithNotesModel> {
        var list = getAllTuningWithNotesUseCase.call(Unit).sortedWith(
            compareByDescending<TuningWithNotesModel> { it.tuning.favourite }.thenBy { it.tuning.id }
        )
        var finalList = list.take(param)
        return finalList
    }

}