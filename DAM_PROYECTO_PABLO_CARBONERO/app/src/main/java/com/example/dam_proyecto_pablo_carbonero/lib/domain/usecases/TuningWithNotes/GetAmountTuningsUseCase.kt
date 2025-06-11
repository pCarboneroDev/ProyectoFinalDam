package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class GetAmountTuningsUseCase @Inject constructor(
    private val getAllTuningWithNotesUseCase: GetAllTuningWithNotesUseCase
    /**
     * Caso de uso que devuelve una determinada cantidad de afinaciones con sus notas
     * @param param recibe un Int con la cantidad de elementos que desea recibir
     */
): UseCase<Int, List<TuningWithNotesModel>> {
    override suspend fun call(param: Int): List<TuningWithNotesModel> {
        var list = getAllTuningWithNotesUseCase.call(Unit).sortedWith(
            compareByDescending<TuningWithNotesModel> { it.tuning.favourite }.thenBy { it.tuning.id }
        )
        var finalList = list.take(param)
        return finalList
    }

}