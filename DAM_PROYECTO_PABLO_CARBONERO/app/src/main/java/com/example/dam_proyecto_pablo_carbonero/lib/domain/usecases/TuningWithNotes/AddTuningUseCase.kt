package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject

class AddTuningUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val notesRepository: MusicNoteRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
): UseCase<TuningWithNotesModel, Boolean> {

    override suspend fun call(param: TuningWithNotesModel): Boolean {
        var newTuning = param.tuning

        val newTuningId = tuningRepository.insertTuning(newTuning)

        param.noteList.map { note ->
            val insert = TuningMusicNote( tuningId =  newTuningId, noteId =  note.id)
            tuningMusicNoteRepository.insertTuningMusicNote(insert)
        }

        return TODO("Provide the return value")
    }
}