package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    val songDao: SongDao
): SongRepository {
    override suspend fun getAllSongs(): List<Song> {
        return songDao.getAllSongs()
    }

    override suspend fun getSongById(id: Long): Song {
        return songDao.getSongById(id)
    }

    override suspend fun insertSong(song: Song): Long {
        return songDao.insertSong(song)
    }

    override suspend fun updateSong(song: Song): Int {
        return songDao.updateSong(song)
    }

    override suspend fun deleteSong(id: Long): Int {
        return songDao.deleteSong(id)
    }

    // PAGING
    override fun getPagedSong(): PagingSource<Int, Song> {
        return songDao.getPagedSongs()
    }

    override fun getPagedSongsDesc(): PagingSource<Int, Song> {
        return songDao.getPagedSongsDesc()
    }

    override fun getPagedSongsByName(): PagingSource<Int, Song> {
        return songDao.getPagedSongsByName()
    }


}