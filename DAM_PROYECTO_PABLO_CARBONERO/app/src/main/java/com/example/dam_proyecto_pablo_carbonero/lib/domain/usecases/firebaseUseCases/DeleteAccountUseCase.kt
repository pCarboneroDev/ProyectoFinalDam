package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<String, Boolean> {
    /**
     * Usecase para eliminar una cuenta de firebase
     * @param param la contraseña del usuario para la reautenticación
     * @return boolean indicando si se ha completado
     */
    override suspend fun call(param: String): Boolean {
        return firebaseRepository.deleteAccount(param)
    }
}