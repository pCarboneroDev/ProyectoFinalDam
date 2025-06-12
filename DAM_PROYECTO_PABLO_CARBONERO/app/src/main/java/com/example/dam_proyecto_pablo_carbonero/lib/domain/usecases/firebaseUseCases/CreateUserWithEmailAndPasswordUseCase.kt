package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.UserParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CreateUserWithEmailAndPasswordUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): UseCase<UserParams, FirebaseUser?> {
    /**
     * Usecase parra la creación de un nuevo usuario en Firebase
     * @param param un objeto de UserParams con los datos del usuario
     * @return el usuario de firebase
     */
    override suspend fun call(param: UserParams): FirebaseUser? {
        return firebaseRepository.createUserWithEmailAndPassword(param.email, param.password)
    }
}