package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class DeleteSongUseCase @Inject constructor(
    private val songRepository: SongRepository
): UseCase<Long, Int> {
    override suspend fun call(param: Long): Int {
        return songRepository.deleteSong(param)
    }
}