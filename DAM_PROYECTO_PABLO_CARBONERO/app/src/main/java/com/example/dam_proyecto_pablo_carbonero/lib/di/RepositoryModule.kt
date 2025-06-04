package com.example.dam_proyecto_pablo_carbonero.lib.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.FirebaseDatasource
import com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.repository_impl.FirebaseRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.MusicNoteRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.SongRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.TuningMusicNoteRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.TuningRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.UserPreferencesRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMusicNoteRepository(dao: MusicNoteDao): MusicNoteRepository {
        return MusicNoteRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTuningRepository(dao: TuningDao): TuningRepository {
        return TuningRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTuningMusicNoteRepository(dao: TuningMusicNoteDao): TuningMusicNoteRepository {
        return TuningMusicNoteRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSongRepository(dao: SongDao): SongRepository {
        return SongRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseDatasource: FirebaseDatasource): FirebaseRepository{
        return FirebaseRepositoryImpl(firebaseDatasource)
    }
}