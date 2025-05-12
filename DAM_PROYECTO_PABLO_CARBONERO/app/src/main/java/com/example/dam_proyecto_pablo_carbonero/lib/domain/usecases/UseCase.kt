package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases

interface UseCase<Input, Output> {
    suspend fun call(param: Input): Output
}