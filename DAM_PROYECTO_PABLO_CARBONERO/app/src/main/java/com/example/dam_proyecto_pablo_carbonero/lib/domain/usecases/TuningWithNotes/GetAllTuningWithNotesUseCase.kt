package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import javax.inject.Inject

class GetAllTuningWithNotesUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val notesRepository: MusicNoteRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository
): UseCase<Unit, List<TuningWithNotesModel>> {

    /**
     * Caso de uso que devuelve una lista con todas las Afinaciones y sus notas de la bbdd
     * @param param es de tipo Unit, es decir, recibe nada
     * @return lista de afinaciones con sus notas
     */
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