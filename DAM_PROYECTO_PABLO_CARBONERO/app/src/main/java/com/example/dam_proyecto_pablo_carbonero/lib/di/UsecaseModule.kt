package com.example.dam_proyecto_pablo_carbonero.lib.di

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
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
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.GetAmountNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.MusicNoteUseCases.InsertAllMusicNotesUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.DeleteSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetPagedSongsUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.GetSongWithTuningByIdUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases.SearchSongUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes.UpdateFavouriteUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateBackupUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.CreateUserWithEmailAndPasswordUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DeleteAccountUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DeleteCloudDataUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.DownloadBackupUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.GetCurrentUserUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.GetDatesInfoUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SendPasswordResetEmailUseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases.SignInWithEmailAndPasswordUseCase
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
    fun provideGetAmountNotes(
        noteRepository: MusicNoteRepository
    ): GetAmountNotesUseCase {
        return GetAmountNotesUseCase(noteRepository)
    }

    @Provides
    @Singleton
    fun provideInsertTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        prefsRepo: UserPreferencesRepository
    ): InsertTuningUseCase {
        return InsertTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository,
            prefsRepo
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideDeleteTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        prefsRepo: UserPreferencesRepository
    ): DeleteTuningUseCase {
        return DeleteTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository,
            prefsRepo
        ) // Your implementation
    }

    @Provides
    @Singleton
    fun provideUpdateTuning(
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        prefsRepo: UserPreferencesRepository
    ): UpdateTuningUseCase {
        return UpdateTuningUseCase(
            tuningRepository,
            tuningMusicNoteRepository,
            prefsRepo
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
        tuningRepository: TuningRepository,
        prefsRepo: UserPreferencesRepository
    ): GetAllTuningUseCase{
        return GetAllTuningUseCase(
            tuningRepository,
            prefsRepo
        )
    }

    @Provides
    @Singleton
    fun provideUpdateFavouriteUseCase(
        tuningRepository: TuningRepository,
        prefsRepo: UserPreferencesRepository
    ): UpdateFavouriteUseCase {
        return UpdateFavouriteUseCase(
            tuningRepository,
            prefsRepo
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
        songRepository: SongRepository,
        prefsRepo: UserPreferencesRepository
    ): InsertSongUseCase{
        return InsertSongUseCase(
            songRepository,
            prefsRepo
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
        songRepository: SongRepository,
        prefsRepo: UserPreferencesRepository
    ): UpdateSongUseCase{
        return UpdateSongUseCase(
            songRepository,
            prefsRepo
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

    @Provides
    @Singleton
    fun provideGetPagedSong(
        songRepository: SongRepository
    ): GetPagedSongsUseCase{
        return GetPagedSongsUseCase(
            songRepository
        )
    }

    @Provides
    @Singleton
    fun provideSearchSong(
        songRepository: SongRepository
    ): SearchSongUseCase{
        return SearchSongUseCase(
            songRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetSongWithTuningById(
        songRepository: SongRepository,
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        musicNoteRepository: MusicNoteRepository,
    ): GetSongWithTuningByIdUseCase{
        return GetSongWithTuningByIdUseCase(
            songRepository,
            tuningRepository,
            tuningMusicNoteRepository,
            musicNoteRepository
        )
    }

    @Provides
    @Singleton
    fun provideCreateBackup(
        musicNoteRepository: MusicNoteRepository,
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        songRepository: SongRepository,
        prefsRepo: UserPreferencesRepository
    ): CreateBackupUseCase{
        return CreateBackupUseCase(
            musicNoteRepository,
            tuningRepository,
            tuningMusicNoteRepository,
            songRepository,
            prefsRepo
        )
    }

    @Provides
    @Singleton
    fun provideDownloadBackup(
        musicNoteRepository: MusicNoteRepository,
        tuningRepository: TuningRepository,
        tuningMusicNoteRepository: TuningMusicNoteRepository,
        songRepository: SongRepository,
        prefsRepo: UserPreferencesRepository
    ): DownloadBackupUseCase{
        return DownloadBackupUseCase(
            musicNoteRepository,
            tuningRepository,
            tuningMusicNoteRepository,
            songRepository,
            prefsRepo
        )
    }

    @Provides
    @Singleton
    fun provideGetCurrentUser(
        firebaseRepository: FirebaseRepository
    ): GetCurrentUserUseCase{
        return GetCurrentUserUseCase(
            firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideCreateUserWithEmailAndPassword(
        firebaseRepository: FirebaseRepository
    ): CreateUserWithEmailAndPasswordUseCase{
        return CreateUserWithEmailAndPasswordUseCase(
            firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideSignInWithEmailAndPassword(
        firebaseRepository: FirebaseRepository
    ): SignInWithEmailAndPasswordUseCase{
        return SignInWithEmailAndPasswordUseCase(
            firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetDatesInfoUseCase(
        prefsRepo: UserPreferencesRepository,
        firebaseRepository: FirebaseRepository
    ): GetDatesInfoUseCase{
        return GetDatesInfoUseCase(
            prefsRepo, firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideSendPasswordResetEmailUseCase(
        firebaseRepository: FirebaseRepository
    ): SendPasswordResetEmailUseCase{
        return SendPasswordResetEmailUseCase(
            firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideDeleteCloudDataUseCase(
        firebaseRepository: FirebaseRepository
    ): DeleteCloudDataUseCase{
        return DeleteCloudDataUseCase(
            firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        firebaseRepository: FirebaseRepository
    ): DeleteAccountUseCase{
        return DeleteAccountUseCase(
            firebaseRepository
        )
    }
}