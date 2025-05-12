package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository

class GetMusicNoteByIdUseCase(
    private val repo: MusicNoteRepository
) : UseCase<Long, MusicNote> {
    override suspend fun call(param: Long): MusicNote {
        return repo.getMusicNoteById(param)
    }
}
