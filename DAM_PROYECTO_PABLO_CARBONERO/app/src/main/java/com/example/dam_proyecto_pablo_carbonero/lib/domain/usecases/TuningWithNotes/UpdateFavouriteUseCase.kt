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
): UseCase<Tuning, Unit> {
    private val maxTuningFavs = 10

    /**
     * Caso de uso que se encarga de poner o quitar una afinación como favorita aplicando la lógica de negocio necesaria,
     * sí es el número va a superar el límite lanza una FullFavouriteTuningsException
     * @param param objeto de Tuning que se va a poner o eliminar de favorito
     */
    override suspend fun call(param: Tuning) {
        val date = Timestamp.now().toDate()
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

        prefsRepo.setLastModificationDate(
            formatter.format(date)
        )

        val tuningList = tuningRepository.getAllTunings().filter { it.favourite == true }

        // si se va a marcar como favourite = true y el límite ha sido alcanzado
        if(param.favourite && tuningList.size >= maxTuningFavs){
            throw FullFavouriteTuningsException("You've reached the limit of favourite tunings")
        }

        tuningRepository.updateTuningFavourite(param.id, param.favourite)
    }
}