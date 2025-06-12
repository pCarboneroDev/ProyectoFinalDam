package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class GetSongWithTuningByIdUseCase @Inject constructor(
    private val songRepository: SongRepository,
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository,
    private val musicNoteRepository: MusicNoteRepository,
): UseCase<Long, SongWithTuning> {
    /**
     * Usecase que obtiene la canción con su afinación de la bbdd
     * @param param id de la canción
     * @return objeto de SongWithTuning con la canción y afinación correspondiente
     */
    override suspend fun call(param: Long): SongWithTuning {
        var songTuning: SongWithTuning
        try {
            val song = songRepository.getSongById(param)
            val tuning = tuningRepository.getTuningById(song.tuningId)
            val noteIdList = tuningMusicNoteRepository.getNotesIdFromTuningId(tuning.id)
            val noteList = mutableListOf<MusicNote>()

            for(id in noteIdList){
                val note = musicNoteRepository.getMusicNoteById(id)
                noteList.add(note)
            }

            noteList.sort()

            songTuning = SongWithTuning(tuning, song, noteList)
        }catch (e: Exception){
            throw e;
        }
        return songTuning
    }
}