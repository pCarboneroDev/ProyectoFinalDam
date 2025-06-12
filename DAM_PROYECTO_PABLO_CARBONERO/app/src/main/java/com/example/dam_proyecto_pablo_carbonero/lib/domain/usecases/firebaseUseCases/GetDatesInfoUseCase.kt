package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class GetDatesInfoUseCase @Inject constructor(
    private val prefsRepository: UserPreferencesRepository,
    private val firebaseRepository: FirebaseRepository
): UseCase<Unit, List<String>> {
    /**
     * USecase que devuelve las fechas de modificación de la bbdd y de firebase
     */
    override suspend fun call(param: Unit): List<String> {
        var localDate = ""
        var cloudDate = ""
        try {
            localDate = prefsRepository.getLastModificationDate()
            cloudDate = firebaseRepository.getCurrentDate()
        }catch (e: Exception){
            throw e;
        }

        return listOf(localDate, cloudDate)
    }
}