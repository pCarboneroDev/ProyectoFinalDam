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
): UseCase<SongWithTuning, Long> {
    override suspend fun call(param: SongWithTuning): Long {
        try{
            val id = songRepo.insertSong(param.song)
            if(id != 0L){

                val date = Timestamp.now().toDate()
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                //Log.d("LA FECHA:", formatter.format(date))
                prefsRepo.setLastModificationDate(
                    formatter.format(date)
                )
            }
            return id
        }
        catch (e: Exception){
            throw e;
        }
    }
}