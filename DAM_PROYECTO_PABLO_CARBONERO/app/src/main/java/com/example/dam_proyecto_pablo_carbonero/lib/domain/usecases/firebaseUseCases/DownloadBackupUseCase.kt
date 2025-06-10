package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.FirebaseRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.MusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.SongRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningMusicNoteRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
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
    private val musicNoteRepository: MusicNoteRepository,
    private val tuningRepository: TuningRepository,
    private val tuningMusicNoteRepository: TuningMusicNoteRepository,
    private val songRepository: SongRepository,
    private val prefsRepository: UserPreferencesRepository,
    private val firebaseRepository: FirebaseRepository
) : UseCase<Unit, Boolean> {
    override suspend fun call(param: Unit): Boolean {

        val doc = firebaseRepository.downloadBackup()

        if (doc != null && doc.exists()) {
            val notes = doc["MusicNote"] as? List<Map<String, Any>>
            val songs = doc["Song"] as? List<Map<String, Any>>
            val tunings = doc["Tuning"] as? List<Map<String, Any>>
            val tuningMusicNote = doc["TuningMusicNote"] as? List<Map<String, Any>>


            // Convertir a tus clases Kotlin
            val restoredNotes = notes?.map { it.toMusicNote() }
            val restoredTunings = tunings?.map { it.toTuning() }
            val restoredTuningMusicNote = tuningMusicNote?.map { it.toTuningMusicNote() }
            val restoredSong = songs?.map { it.toSong() }


            tuningMusicNoteRepository.deleteAllTuningMusicNote()
            musicNoteRepository.deleteAllMusicNotes()
            tuningRepository.deleteAllTunings()
            songRepository.deleteAllSongs()

            musicNoteRepository.insertAllMusicNotes(restoredNotes ?: emptyList())
            tuningRepository.insertAllTuning(restoredTunings ?: emptyList())
            tuningMusicNoteRepository.insertAllTuningMusicNote(
                restoredTuningMusicNote ?: emptyList()
            )
            songRepository.insertAllSongs(restoredSong ?: emptyList())

            val date = Timestamp.now().toDate()
            val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
            prefsRepository.setLastModificationDate(formatter.format(date))

            return true
        }
        else {
            return false
        }
    }
}