package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.utils.SortOption
import javax.inject.Inject

class GetPagedSongsUseCase @Inject constructor(
    private val songRepository: SongRepository,
): UseCase<SortOption, PagingSource<Int, Song>> {
    /**
     * caso de usu asíncrono para obtener las canciones paginadas.
     * La librería paging trabaja con funciones síncronas así que no usar
     */
    override suspend fun call(param: SortOption): PagingSource<Int, Song> {
        return songRepository.getPagedSong()
    }

    /**
     * Metodo asíncrono para obtener las canciones paginadas por la librería Paging
     * @param param la opción para el filtrado
     * @return lista de canciones
     */
    fun synchronousCall(param: SortOption): PagingSource<Int, Song> {
        return if (param == SortOption.DATE_ASCENDING)
            songRepository.getPagedSong()
        else if (param == SortOption.DATE_DESCENDING)
            songRepository.getPagedSongsDesc()
        else if (param == SortOption.NAME_ASCENDING)
            songRepository.getPagedSongsByName()
        else if (param == SortOption.NAME_DESCENDING)
            songRepository.getPagedSongsByNameDesc()
        else if (param == SortOption.BAND_NAME)
            songRepository.getPagedSongsByBandName()
        else if (param == SortOption.BAND_NAME_DESCENDING)
            songRepository.getPagedSongsByBandNameDesc()
        else songRepository.getPagedSong()
    }
}