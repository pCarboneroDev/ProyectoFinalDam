package com.example.dam_proyecto_pablo_carbonero.lib.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote

@Database(entities = [MusicNote::class, Tuning::class, TuningMusicNote::class, Song::class], version = 7)
abstract class TuningDatabase: RoomDatabase(){
    abstract fun musicNoteDao(): MusicNoteDao
    abstract fun tuningDao(): TuningDao
    abstract fun tuningMusicNoteDao(): TuningMusicNoteDao
    abstract fun songDao(): SongDao
}