package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository

class InsertAllMusicNotesUseCase(
    private val repo: MusicNoteRepository
) : UseCase<List<MusicNote>, List<Long>> {
    override suspend fun call(param: List<MusicNote>): List<Long> {
        return repo.insertAllMusicNotes(param)
    }
}
