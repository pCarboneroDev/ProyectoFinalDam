package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import javax.inject.Inject

class UpdateSongUseCase @Inject constructor(
    private val songRepository: SongRepository
): UseCase<Song, Int> {
    override suspend fun call(param: Song): Int {
        return songRepository.updateSong(param)
    }

}