package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject

class GetAllTuningWithNotesUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val notesRepository: MusicNoteRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
): UseCase<Unit, List<TuningWithNotesModel>> {

    override suspend fun call(param: Unit): List<TuningWithNotesModel> {
        var tuningList = tuningRepository.getAllTunings()

        var tuningWithNotesList = tuningList.map { tuning ->
            var notesId = tuningMusicNoteRepository.getNotesIdFromTuningId(tuning.id)
            val notes = notesId.map { noteId ->
                notesRepository.getMusicNoteById(noteId)
            }
            TuningWithNotesModel(tuning, notes)
        }

        return tuningWithNotesList
    }
}