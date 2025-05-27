package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import androidx.paging.PagingSource
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import javax.inject.Inject

class GetPagedSongsUseCase @Inject constructor(
    private val songRepository: SongRepository,
): UseCase<SortOption, PagingSource<Int, Song>> {
    override suspend fun call(param: SortOption): PagingSource<Int, Song> {
        return songRepository.getPagedSong()
    }

    fun synchronousCall(param: SortOption): PagingSource<Int, Song> {
        return if (param == SortOption.DATE_ASCENDING)
            songRepository.getPagedSong()
        else if (param == SortOption.DATE_DESCENDING)
            songRepository.getPagedSongsDesc()
        else if (param == SortOption.NAME_ASCENDING)
            songRepository.getPagedSongsByName()
        else songRepository.getPagedSong()
    }
}