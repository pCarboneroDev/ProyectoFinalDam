package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class UpdateSongUseCase @Inject constructor(
    private val songRepository: SongRepository,
    private val prefsRepo: UserPreferencesRepository
): UseCase<Song, Int> {
    /**
     * Usecase para actualizar una canción de la bbdd
     * @param param canción actualzada
     * @return nº de filas afectadas
     */
    override suspend fun call(param: Song): Int {
        val date = Timestamp.now().toDate()
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

        prefsRepo.setLastModificationDate(
            formatter.format(date)
        )
        return songRepository.updateSong(param)
    }

}