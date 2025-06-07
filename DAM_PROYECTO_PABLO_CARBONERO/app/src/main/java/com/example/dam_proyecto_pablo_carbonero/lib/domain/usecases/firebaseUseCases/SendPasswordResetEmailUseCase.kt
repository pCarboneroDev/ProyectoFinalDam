package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<String, Boolean> {
    override suspend fun call(param: String): Boolean {
        return firebaseRepository.sendPasswordResetEmail(param)
    }
}