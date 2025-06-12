package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    val songDao: SongDao
): SongRepository {

    /**
     * Accede a songDao para obtener todas las canciondes de la bbdd
     * @return lista de canciones
     */
    override suspend fun getAllSongs(): List<Song> {
        return songDao.getAllSongs()
    }

    /**
     * Accede a songDao para obtener una canción por id de la bbdd
     * @param id de la cancion
     * @return Song
     */
    override suspend fun getSongById(id: Long): Song {
        return songDao.getSongById(id)
    }

    /**
     * Accede a songDao para obtener todas las canciondes de la bbdd
     * @param song
     * @return lista de canciones
     */
    override suspend fun insertSong(song: Song): Long {
        return songDao.insertSong(song)
    }

    /**
     * Accede a songDao para obtener todas las canciondes de la bbdd
     * @param song
     * @return filas afectadas
     */
    override suspend fun updateSong(song: Song): Int {
        return songDao.updateSong(song)
    }

    /**
     * Accede a songDao para obtener todas las canciondes de la bbdd
     * @param id
     * @return filas afectadas
     */
    override suspend fun deleteSong(id: Long): Int {
        return songDao.deleteSong(id)
    }

    /**
     * Accede a la songDao para insertar todas las canciones
     * @param songs lista de canciones
     * @return lista de canciones
     */
    override suspend fun insertAllSongs(songs: List<Song>): List<Long> {
        return songDao.insertAllSongs(songs)
    }

    /**
     * Accede a la songDao para borrar todas las canciones de la bbdd
     * @return filas afectadas
     */
    override suspend fun deleteAllSongs(): Int {
        return songDao.deleteAllSongs()
    }

    // PAGING

    /**
     * Accede a la songDao para obtener las canciones paginadas
     * @return PagingSource
     */
    override fun getPagedSong(): PagingSource<Int, Song> {
        return songDao.getPagedSongs()
    }

    /**
     * Accede a la songDao para obtener las canciones paginadas en orden descendente
     * @return PagingSource
     */
    override fun getPagedSongsDesc(): PagingSource<Int, Song> {
        return songDao.getPagedSongsDesc()
    }

    /**
     * Accede a la songDao para obtener las canciones paginadas ordenadaspor nombre
     * @return PagingSource
     */
    override fun getPagedSongsByName(): PagingSource<Int, Song> {
        return songDao.getPagedSongsByName()
    }

    /**
     * Accede a la songDao para obtener las canciones paginadas ordenadas por nombredescendente
     * @return PagingSource
     */
    override fun getPagedSongsByNameDesc(): PagingSource<Int, Song> {
        return songDao.getPagedSongsByNameDesc()
    }

    /**
     * Accede a la songDao para obtener las canciones paginadas ordenadas por nombre de la banda
     * @return PagingSource
     */
    override fun getPagedSongsByBandName(): PagingSource<Int, Song> {
        return songDao.getPagedSongsByBandName()
    }


    /**
     * Accede a la songDao para obtener las canciones paginadas ordenadas por nombre de banda descendente
     * @return PagingSource
     */
    override fun getPagedSongsByBandNameDesc(): PagingSource<Int, Song> {
        return songDao.getPagedSongsByBandNameDesc()
    }

    /**
     * Accede a la songDao para obtener las canciones paginadas por query
     * @return PagingSource
     */
    override fun searchSong(query: String): PagingSource<Int, Song> {
        return songDao.searchSong(query)
    }
}