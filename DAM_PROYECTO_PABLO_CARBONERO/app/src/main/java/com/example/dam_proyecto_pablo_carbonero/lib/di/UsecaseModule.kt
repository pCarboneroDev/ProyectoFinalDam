package com.example.dam_proyecto_pablo_carbonero.lib.di

import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAllNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetAllSongsUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetSongByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.InsertSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.UpdateSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningUseCases.GetAllTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.DeleteTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.InsertTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetAllTuningWithNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.GetTuningByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateTuningUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.InsertAllMusicNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.DeleteSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateFavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {

    @Provides
    @Singleton
    fun provideGetAllMusicNotes(
        notesRepository: MusicNoteRepository
    ): GetAllNotesUseCase {
        return GetAllNotesUseCase(
            notesRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideInsertAllMusicNotes(
        notesRepository: MusicNoteRepository
    ): InsertAllMusicNotesUseCase {
        return InsertAllMusicNotesUseCase(
            notesRepository
        )
    }

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
    fun provideDeleteTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository
    ): DeleteTuningUseCase {
        return DeleteTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideUpdateTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository
    ): UpdateTuningUseCase {
        return UpdateTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideGetTuningById(
        tuningRepository: TuningRepository,
        noteRepository: MusicNoteRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository
    ): GetTuningByIdUseCase {
        return GetTuningByIdUseCase(
            noteRepository,
            tuningRepository,
            tuningMusicNoteRepository
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideGetAllTunings(
        tuningRepository: TuningRepository
    ): GetAllTuningUseCase{
        return GetAllTuningUseCase(
            tuningRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateFavouriteUseCase(
        tuningRepository: TuningRepository
    ): UpdateFavouriteUseCase {
        return UpdateFavouriteUseCase(
            tuningRepository
        ) // Your implementation
    }


    @Provides
    @Singleton
    fun provideGetAllSongs(
        songRepository: SongRepository,
        tuningRepository: TuningRepository
    ): GetAllSongsUseCase{
        return GetAllSongsUseCase(
            songRepository,
            tuningRepository
        )
    }

    @Provides
    @Singleton
    fun provideInsertSong(
        songRepository: SongRepository
    ): InsertSongUseCase{
        return InsertSongUseCase(
            songRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetSongById(
        songRepository: SongRepository
    ): GetSongByIdUseCase{
        return GetSongByIdUseCase(
            songRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateSong(
        songRepository: SongRepository
    ): UpdateSongUseCase{
        return UpdateSongUseCase(
            songRepository
        )
    }

    @Provides
    @Singleton
    fun provideDeleteDong(
        songRepository: SongRepository
    ): DeleteSongUseCase{
        return DeleteSongUseCase(
            songRepository
        )
    }


}