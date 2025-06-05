package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.exceptions.FullFavouriteTuningsException
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class UpdateFavouriteUseCase(
    private val tuningRepository: TuningRepository,
    private val prefsRepo: UserPreferencesRepository
): UseCase<Tuning, Boolean> {
    private val maxTuningFavs = 10
    override suspend fun call(param: Tuning): Boolean {

        val date = Timestamp.now().toDate()
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

        prefsRepo.setLastModificationDate(
            formatter.format(date)
        )

        val tuningList = tuningRepository.getAllTunings().filter { it.favourite == true}

        if(param.favourite && tuningList.size >= maxTuningFavs){
            throw FullFavouriteTuningsException("You've reached the limit of favourite tunings")
        }
        try{
            tuningRepository.updateTuningFavourite(param.id, param.favourite)
        }catch (e: Exception){
            throw e
        }

        return true
    }
}