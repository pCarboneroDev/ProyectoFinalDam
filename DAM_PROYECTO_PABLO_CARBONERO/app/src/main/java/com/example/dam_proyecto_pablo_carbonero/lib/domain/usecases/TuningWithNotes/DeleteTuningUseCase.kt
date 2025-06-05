package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DeleteTuningUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository,
    private val prefsRepo: UserPreferencesRepository
): UseCase<TuningWithNotesModel, Int> {
    override suspend fun call(param: TuningWithNotesModel): Int {
        try {
            val date = Timestamp.now().toDate()
            val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

            prefsRepo.setLastModificationDate(
                formatter.format(date)
            )

            tuningMusicNoteRepository.deleteNoteByTuningId(param.tuning.id)
            val rowsAffected = tuningRepository.deleteTuningById(param.tuning.id)
            return rowsAffected
        }
        catch (e: Exception){
            throw e
        }
        return 0
    }
}