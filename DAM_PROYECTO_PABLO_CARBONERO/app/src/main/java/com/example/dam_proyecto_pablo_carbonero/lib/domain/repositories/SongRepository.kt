package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song

interface SongRepository {
    suspend fun getAllSongs(): List<Song>
    suspend fun getSongById(id: Long): Song
    suspend fun insertSong(song: Song): Long
    suspend fun updateSong(song: Song): Int
}