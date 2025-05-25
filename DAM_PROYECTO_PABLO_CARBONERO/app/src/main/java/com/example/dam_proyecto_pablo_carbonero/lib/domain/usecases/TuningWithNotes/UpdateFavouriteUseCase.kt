package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.FullFavouriteTuningsException

class UpdateFavouriteUseCase(
    private val tuningRepository: TuningRepository
): UseCase<Tuning, Boolean> {
    private val maxTuningFavs = 10
    override suspend fun call(param: Tuning): Boolean {
        val tuningList = tuningRepository.getAllTunings().filter { it.favourite == true}

        if(param.favourite && tuningList.size >= maxTuningFavs){
            throw FullFavouriteTuningsException("Max number of tuning added to favourites")
        }

        try{
            tuningRepository.updateTuningFavourite(param.id, param.favourite)
        }catch (e: Exception){
            throw e
        }

        return true
    }
}