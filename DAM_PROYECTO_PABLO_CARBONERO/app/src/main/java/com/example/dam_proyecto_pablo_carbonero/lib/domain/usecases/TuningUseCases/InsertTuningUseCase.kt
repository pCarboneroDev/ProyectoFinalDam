package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.TuningParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject

class InsertTuningUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
) : UseCase<TuningParams, Boolean> {
    override suspend fun call(param: TuningParams): Boolean {
        var saved = true
        try {
            val tuningId = tuningRepository.insertTuning(param.tuning)

            param.noteList.map { note ->
                var insert = TuningMusicNote(tuningId = tuningId, noteId = note.id)
                tuningMusicNoteRepository.insertTuningMusicNote(insert)
            }
        }catch (e: Exception){
            saved = false;
            throw e
        }
        return saved
    }
}
