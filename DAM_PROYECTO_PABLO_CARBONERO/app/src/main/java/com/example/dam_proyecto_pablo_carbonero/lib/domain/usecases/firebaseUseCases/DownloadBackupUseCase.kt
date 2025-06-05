package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.toMusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.toSong
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.toTuning
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.toTuningMusicNote
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DownloadBackupUseCase @Inject constructor(
    private val musicNoteDao: MusicNoteDao,
    private val tuningDao: TuningDao,
    private val tuningMusicNoteDao: TuningMusicNoteDao,
    private val songDao: SongDao,
    private val prefsRepository: UserPreferencesRepository
) : UseCase<Unit, Boolean> {
    override suspend fun call(param: Unit): Boolean {
        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid ?: return false

        firestore.collection("backups")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val notes = doc["MusicNote"] as? List<Map<String, Any>>
                    val songs = doc["Song"] as? List<Map<String, Any>>
                    val tunings = doc["Tuning"] as? List<Map<String, Any>>
                    val tuningMusicNote = doc["TuningMusicNote"] as? List<Map<String, Any>>


                    // Convertir a tus clases Kotlin
                    val restoredNotes = notes?.map { it.toMusicNote() }
                    val restoredTunings = tunings?.map { it.toTuning() }
                    val restoredTuningMusicNote = tuningMusicNote?.map { it.toTuningMusicNote() }
                    val restoredSong = songs?.map { it.toSong() }

                    // Guardar en Room reemplazando lo anterior
                    CoroutineScope(Dispatchers.IO).launch {
                        tuningMusicNoteDao.deleteAllTuningMusicNote()
                        musicNoteDao.deleteAllMusicNotes()
                        tuningDao.deleteAllTunings()
                        songDao.deleteAllSongs()

                        musicNoteDao.insertAllMusicNotes(restoredNotes ?: emptyList())
                        tuningDao.insertAllTuning(restoredTunings ?: emptyList())
                        tuningMusicNoteDao.insertAllTuningMusicNote(restoredTuningMusicNote ?: emptyList())
                        songDao.insertAllSongs(restoredSong ?: emptyList())

                        val date = Timestamp.now().toDate()
                        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                        prefsRepository.setLastModificationDate(formatter.format(date))
                    }
                }
            }

        return true
    }

}