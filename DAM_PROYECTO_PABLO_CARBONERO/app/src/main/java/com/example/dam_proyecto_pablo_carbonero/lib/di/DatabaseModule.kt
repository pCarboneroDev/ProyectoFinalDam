package com.example.dam_proyecto_pablo_carbonero.lib.di

import android.content.Context
import androidx.room.Room
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.database.TuningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TuningDatabase {
        return Room.databaseBuilder(
            context,
            TuningDatabase::class.java,
            "tuning-database"
        ).build()
    }

    @Provides
    fun provideMusicNoteDao(database: TuningDatabase): MusicNoteDao {
        return database.musicNoteDao()
    }

    @Provides
    fun provideTuningDao(database: TuningDatabase): TuningDao {
        return database.tuningDao()
    }

    @Provides
    fun provideTuningMusicNoteDao(database: TuningDatabase): TuningMusicNoteDao {
        return database.tuningMusicNoteDao()
    }

    @Provides
    fun provideSongDao(database: TuningDatabase): SongDao {
        return database.songDao()
    }
}