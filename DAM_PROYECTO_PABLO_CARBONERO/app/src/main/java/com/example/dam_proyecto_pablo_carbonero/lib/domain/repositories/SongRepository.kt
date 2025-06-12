package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song

/**
 * Interfaz que define todas las posibles acciones que se pueden realizar con las canciones
 */
interface SongRepository {
    suspend fun getAllSongs(): List<Song>
    suspend fun getSongById(id: Long): Song
    suspend fun insertSong(song: Song): Long
    suspend fun updateSong(song: Song): Int
    suspend fun deleteSong(id: Long): Int
    suspend fun insertAllSongs(songs: List<Song>): List<Long>
    suspend fun deleteAllSongs(): Int
    // paging
    fun getPagedSong(): PagingSource<Int, Song>
    fun getPagedSongsDesc(): PagingSource<Int, Song>
    fun getPagedSongsByName(): PagingSource<Int, Song>
    fun getPagedSongsByNameDesc(): PagingSource<Int, Song>
    fun getPagedSongsByBandName(): PagingSource<Int, Song>
    fun getPagedSongsByBandNameDesc(): PagingSource<Int, Song>
    fun searchSong(query: String): PagingSource<Int, Song>
}