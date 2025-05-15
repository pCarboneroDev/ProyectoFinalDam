package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository

class UpdateTuningUseCase(
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
): UseCase<TuningWithNotesModel, Boolean> {

    override suspend fun call(param: TuningWithNotesModel): Boolean {
        var saved = true
        try{
            tuningRepository.updateTuningName(param.tuning.id, param.tuning.name)
            tuningMusicNoteRepository.deleteNoteByTuningId(param.tuning.id)
            param.noteList.forEach { note ->
                val insert = TuningMusicNote(tuningId = param.tuning.id, noteId = note.id)
                tuningMusicNoteRepository.insertTuningMusicNote(insert)
            }
        }
        catch (e: Exception){
            saved = false
            throw e
        }
        return saved
    }
}