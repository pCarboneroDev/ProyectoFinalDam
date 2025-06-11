package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.TuningWithNotes

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.TuningMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class InsertTuningUseCase @Inject constructor(
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository,
    private val prefsRepo: UserPreferencesRepository
): UseCase<TuningWithNotesModel, Boolean> {

    /**
     * Caso de uso que accede a los repositorios para insertar una nueva afinación y sus notas en la bbdd
     * @param param el objeto que se va a insertar en la bbdd
     * @return boolean indicando si se ha insertado o no
     */
    override suspend fun call(param: TuningWithNotesModel): Boolean {
        var saved = true
        try{
            val date = Timestamp.now().toDate()
            val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())

            prefsRepo.setLastModificationDate(
                formatter.format(date)
            )

            var newTuning = param.tuning

            val newTuningId = tuningRepository.insertTuning(newTuning)

            param.noteList.map { note ->
                val insert = TuningMusicNote( tuningId =  newTuningId, noteId =  note.id)
                tuningMusicNoteRepository.insertTuningMusicNote(insert)
            }
        }
        catch (e: Exception){
            saved = false
            throw e;
        }
        return saved
    }
}