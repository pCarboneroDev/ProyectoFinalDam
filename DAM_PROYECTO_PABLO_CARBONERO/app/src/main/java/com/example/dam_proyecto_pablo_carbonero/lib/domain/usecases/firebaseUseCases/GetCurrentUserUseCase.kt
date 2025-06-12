package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<Unit, FirebaseUser?> {
    /**
     * Caso de uso para obtener el usuario que tiene sesión iniciada
     * @return el usuario de firebase, null si no hay nunguna sesión
     */
    override suspend fun call(param: Unit): FirebaseUser? {
        return firebaseRepository.getCurrentUser()
    }
}