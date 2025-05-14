package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject

class GetTuningByIdUseCase @Inject constructor(
    private val musicNoteRepository: MusicNoteRepository,
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
): UseCase<Long, TuningWithNotesModel> {

    override suspend fun call(param: Long): TuningWithNotesModel {
        var tuningModel: TuningWithNotesModel
        try {
            val tuning = tuningRepository.getTuningById(param)
            val noteIds = tuningMusicNoteRepository.getNotesIdFromTuningId(param)

            val noteList = noteIds.map { id ->
                val note = musicNoteRepository.getMusicNoteById(id)
                note// se añade a la lista de notas
            }

            var finalList = noteList.toMutableList()
            finalList.sort()

            tuningModel = TuningWithNotesModel(tuning, finalList)
        }
        catch (e: Exception){
            throw e
        }
        return tuningModel
    }
}