package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject


class InsertSongUseCase @Inject constructor(
    private val songRepo: SongRepository
): UseCase<SongWithTuning, Long> {
    override suspend fun call(param: SongWithTuning): Long {
        try{
            val id = songRepo.insertSong(param.song)
            return id
        }
        catch (e: Exception){
            return 0
        }
    }
}