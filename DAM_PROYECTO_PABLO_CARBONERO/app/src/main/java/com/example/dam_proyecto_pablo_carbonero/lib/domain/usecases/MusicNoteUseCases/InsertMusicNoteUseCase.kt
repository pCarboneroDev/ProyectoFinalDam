package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository

class InsertMusicNoteUseCase(
    private val repo: MusicNoteRepository
) : UseCase<MusicNote, Long> {
    override suspend fun call(param: MusicNote): Long {
        return repo.insertMusicNote(param)
    }
}
