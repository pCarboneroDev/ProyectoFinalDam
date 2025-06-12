package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class DeleteSongUseCase @Inject constructor(
    private val songRepository: SongRepository
): UseCase<Long, Int> {
    /**
     * Usecase que borra una canción de la bbdd
     * @param param id de la canción que se va a borrar
     * @return número de filas afectadas
     */
    override suspend fun call(param: Long): Int {
        return songRepository.deleteSong(param)
    }
}