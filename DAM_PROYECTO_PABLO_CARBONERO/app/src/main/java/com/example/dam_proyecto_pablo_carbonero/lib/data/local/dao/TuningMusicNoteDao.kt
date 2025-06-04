package com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote

@Dao
interface TuningMusicNoteDao {
    @Query("Select * FROM TuningMusicNote")
    suspend fun getAllTuningMusicNote():List<TuningMusicNote>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTuningMusicNote(tuningNote: TuningMusicNote): Long

    @Query("Select noteId FROM TuningMusicNote WHERE tuningId = :tuningId")
    suspend fun getNotesIdFromTuningId(tuningId: Long): List<Long>

    @Query("Delete From TuningMusicNote")
    suspend fun deleteAllTuningMusicNote(): Int

    @Query("DELETE FROM TuningMusicNote WHERE tuningId = :tuningId")
    suspend fun deleteNoteByTuningId(tuningId: Long): Int

    @Query("UPDATE TuningMusicNote Set noteId = :noteId WHERE tuningId = :tuningId")
    suspend fun updateTuningNoteId(tuningId: Long, noteId: Long)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllTuningMusicNote(list: List<TuningMusicNote>): List<Long>
}