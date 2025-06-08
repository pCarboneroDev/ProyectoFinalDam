package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class DeleteCloudDataUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<Unit, Boolean> {
    override suspend fun call(param: Unit): Boolean {
        return firebaseRepository.deleteCloudData()
    }
}