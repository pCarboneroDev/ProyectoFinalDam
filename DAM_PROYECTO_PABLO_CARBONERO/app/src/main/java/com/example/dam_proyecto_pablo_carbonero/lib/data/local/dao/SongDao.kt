package com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM Songs")
    suspend fun getAllSongs(): List<Song>

    @Query("SELECT * FROM Songs WHERE id = :id")
    suspend fun getSongById(id: Long): Song

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSong(song: Song): Int

    @Query("DELETE FROM Songs WHERE id = :id")
    suspend fun deleteSong(id: Long): Int

   /* @Query("SELECT * FROM songs WHERE name LIKE '%' || :query || '%' OR bandName LIKE '%' || :query || '%'")
    suspend fun searchSong(query: String): List<Song>*/

    /*@Query("UPDATE Songs SET name = :name, bandName = :name WHERE id = :id")
    suspend fun updateSong(id: Long, name:String, song: Song)*/
}