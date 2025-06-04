package com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao

import androidx.paging.PagingSource
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

    @Query("Delete From Songs")
    suspend fun deleteAllSongs(): Int

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllSongs(songs: List<Song>): List<Long>


    // MÉTODOS PARA PEDIR LA LISTA CON DISTINTO ORDEN DE FORMA PAGINADA

    // por id (fecha)
    @Query("SELECT * FROM Songs ORDER By id")
    fun getPagedSongs(): PagingSource<Int, Song>
    @Query("SELECT * FROM Songs ORDER By id desc")
    fun getPagedSongsDesc(): PagingSource<Int, Song>

    // por nombre
    @Query("SELECT * FROM Songs ORDER By name")
    fun getPagedSongsByName(): PagingSource<Int, Song>
    @Query("SELECT * FROM Songs ORDER By name desc")
    fun getPagedSongsByNameDesc(): PagingSource<Int, Song>

    // por nombre de la banda
    @Query("SELECT * FROM Songs ORDER By bandName")
    fun getPagedSongsByBandName(): PagingSource<Int, Song>
    @Query("SELECT * FROM Songs ORDER By bandName desc")
    fun getPagedSongsByBandNameDesc(): PagingSource<Int, Song>

    @Query("SELECT * FROM songs WHERE name LIKE '%' || :query || '%' OR bandName LIKE '%' || :query || '%'")
    fun searchSong(query: String): PagingSource<Int, Song>

    /*@Query("UPDATE Songs SET name = :name, bandName = :name WHERE id = :id")
    suspend fun updateSong(id: Long, name:String, song: Song)*/
}