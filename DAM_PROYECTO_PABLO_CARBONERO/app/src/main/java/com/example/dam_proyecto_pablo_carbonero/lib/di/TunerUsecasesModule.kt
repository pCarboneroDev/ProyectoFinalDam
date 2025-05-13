package com.example.dam_proyecto_pablo_carbonero.lib.di

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases.InsertTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAllTuningWithNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TunerUsecasesModule {
    @Provides
    @Singleton
    fun provideGetAllTuningWithNotes(
        tuningRepository: TuningRepository,
        noteRepository: MusicNoteRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository
    ): GetAllTuningWithNotesUseCase {
        return GetAllTuningWithNotesUseCase(
            tuningRepository,
            noteRepository,
            tuningMusicNoteRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideInsertTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository
    ): InsertTuningUseCase {
        return InsertTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideGetAllMusicNotes(
        notesRepository: MusicNoteRepository
    ): GetAllNotesUseCase {
        return GetAllNotesUseCase(
            notesRepository
        ) // Your implementation
    }
}