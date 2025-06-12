package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import javax.inject.Inject

class SearchSongUseCase @Inject constructor(
    private val songRepository: SongRepository
): UseCase<String, PagingSource<Int, Song>> {
    /**
     * caso de usu asíncrono para obtener las canciones paginadas.
     * La librería paging trabaja con funciones síncronas así que no usar
     */
    override suspend fun call(param: String): PagingSource<Int, Song> {
        return songRepository.searchSong(param)
    }

    /**
     * llamada síncrona que hace una búsquedad de canciones por una query
     * @param param query
     * @return lista de Song
     */
    fun synchronousCall(param: String): PagingSource<Int, Song> {
        return songRepository.searchSong(param)
    }
}