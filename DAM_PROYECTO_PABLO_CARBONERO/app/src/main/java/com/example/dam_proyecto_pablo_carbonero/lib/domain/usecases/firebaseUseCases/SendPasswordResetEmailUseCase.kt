package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<String, Boolean> {
    /**
     * Usecase para enviar un correo de recuperación de contraseña
     * @param param la dirección a la que se enviará el correo
     * @return boolean indicando si se ha realizado correctamente
     */
    override suspend fun call(param: String): Boolean {
        return firebaseRepository.sendPasswordResetEmail(param)
    }
}