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
) : UseCase<TuningWithNotesModel, Int> {
    /**
     * Caso de uso para borrar una afinación en la bbdd
     * @param param recibe objeto de TuningWithNotesModel que contiene la afinación y las notas se van a borrar
     * @return el número de filas afectadas
     * Sí hay una excepción se captura y gestiona en el vm
     */
    override suspend fun call(param: TuningWithNotesModel): Int {
        val date = Timestamp.now().toDate()
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

        prefsRepo.setLastModificationDate(
            formatter.format(date)
        )
        tuningMusicNoteRepository.deleteNoteByTuningId(param.tuning.id)
        val rowsAffected = tuningRepository.deleteTuningById(param.tuning.id)
        return rowsAffected
    }
}