package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.SongRepository
import javax.inject.Inject

class GetSongByIdUseCase @Inject constructor(
    private val songRepository: SongRepository
): UseCase<Long, Song> {
    override suspend fun call(param: Long): Song {
        return songRepository.getSongById(param)
    }
}