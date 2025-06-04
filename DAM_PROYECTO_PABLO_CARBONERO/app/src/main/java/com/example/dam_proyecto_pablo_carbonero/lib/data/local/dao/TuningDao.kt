package com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning

@Dao
interface TuningDao {
    @Query("Select * FROM Tunings")
    suspend fun getAllTunings():List<Tuning>

    @Query("SELECT * FROM Tunings WHERE id = :id")
    suspend fun getTuningById(id: Long): Tuning

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTuning(tuning: Tuning): Long

    @Query("Delete From Tunings")
    suspend fun deleteAllTunings(): Int

    @Query("Delete From Tunings WHERE id = :id")
    suspend fun deleteTuningById(id: Long): Int

    @Query("UPDATE Tunings Set name = :newName WHERE id = :tuningId")
    suspend fun updateTuningName(tuningId: Long, newName: String)

    @Query("UPDATE Tunings SET favourite = :favourite WHERE id = :tuningId")
    suspend fun updateTuningFavourite(tuningId: Long, favourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllTuning(tuning: List<Tuning>): List<Long>
}