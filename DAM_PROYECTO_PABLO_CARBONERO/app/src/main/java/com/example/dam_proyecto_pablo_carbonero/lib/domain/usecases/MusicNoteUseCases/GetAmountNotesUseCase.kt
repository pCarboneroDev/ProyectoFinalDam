package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.params.AmountNotesParams
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase


class GetAmountNotesUseCase(
    private val musicNoteRepository: MusicNoteRepository
): UseCase<AmountNotesParams, List<MusicNote>> {
    /**
     * Usecase que devuelve un número de MusicNotes de la bbdd
     * @param param int con el valor de notas que se van a devlver
     * @return List de MusicNotes
     */
    override suspend fun call(param: AmountNotesParams): List<MusicNote> {
        return musicNoteRepository.getAmountNotes(param.number, param.lastIndex)
    }
}