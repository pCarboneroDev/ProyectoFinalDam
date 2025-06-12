package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.SongUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class InsertSongUseCase @Inject constructor(
    private val songRepo: SongRepository,
    private val prefsRepo: UserPreferencesRepository
) : UseCase<SongWithTuning, Long> {
    /**
     * usecase que manda a insertar una canción en la bbdd
     * @param param objeto de SongwWithuning con la canción
     * @return id del nuevo elemento insertado, 0 si ocurre un fallo sin excepción
     */
    override suspend fun call(param: SongWithTuning): Long {
        val id = songRepo.insertSong(param.song)
        if (id != 0L) {

            val date = Timestamp.now().toDate()
            val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

            prefsRepo.setLastModificationDate(
                formatter.format(date)
            )
        }
        return id
    }
}