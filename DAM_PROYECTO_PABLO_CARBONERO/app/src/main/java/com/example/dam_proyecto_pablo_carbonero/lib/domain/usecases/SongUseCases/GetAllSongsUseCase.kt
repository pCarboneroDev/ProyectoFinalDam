package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import javax.inject.Inject

class GetAllSongsUseCase @Inject constructor(
    private val songRepo: SongRepository,
    private val tuningRepo: TuningRepository
): UseCase<Unit, List<SongWithTuning>> {
    override suspend fun call(param: Unit): List<SongWithTuning> {
        var list = songRepo.getAllSongs()

        var songModelList = mutableListOf<SongWithTuning>()

        for (song in list){
            var t = tuningRepo.getTuningById(song.tuningId)
            songModelList.add(SongWithTuning(t, song))
        }

        return songModelList
    }
}