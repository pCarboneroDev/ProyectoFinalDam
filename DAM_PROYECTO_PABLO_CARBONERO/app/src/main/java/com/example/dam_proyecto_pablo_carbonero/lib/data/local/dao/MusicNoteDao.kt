package com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote

@Dao
interface MusicNoteDao {
    @Query("Select * FROM MusicNotes")
    suspend fun getAllNotes():List<MusicNote>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMusicNote(note: MusicNote): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllMusicNotes(note: List<MusicNote>): List<Long>

    @Query("Delete From MusicNotes")
    suspend fun deleteAllMusicNotes(): Int

    @Query("Select * FROM MusicNotes WHERE id = :noteId")
    suspend fun getMusicNoteById(noteId: Long): MusicNote
}