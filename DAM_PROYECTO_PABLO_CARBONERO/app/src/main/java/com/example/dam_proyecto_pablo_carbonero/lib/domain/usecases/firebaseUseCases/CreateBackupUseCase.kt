package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import android.util.Log
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.UserPreferencesRepository
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class CreateBackupUseCase @Inject constructor(
    private val musicNoteDao: MusicNoteDao,
    private val tuningDao: TuningDao,
    private val tuningMusicNoteDao: TuningMusicNoteDao,
    private val songDao: SongDao,
    private val prefsRepo: UserPreferencesRepository
): UseCase<Unit, Boolean> {
    override suspend fun call(param: Unit): Boolean {
        val notes = musicNoteDao.getAllNotes()
        val tunings = tuningDao.getAllTunings()
        val tuningMusicNote = tuningMusicNoteDao.getAllTuningMusicNote()
        val songs = songDao.getAllSongs()


        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val date = formatter.parse(prefsRepo.getLastModificationDate())!!
        val timestamp = Timestamp(date)


        val notesMap = mapOf(
            "MusicNote" to notes,
            "Tuning" to tunings,
            "TuningMusicNote" to tuningMusicNote,
            "Song" to songs,
            "lastModificationDate" to timestamp
        )


        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid ?: return false

        firestore.collection("backups")
            .document(userId)
            .set(notesMap)
            .addOnSuccessListener {
                Log.d("Backup", "Datos subidos correctamente")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                Log.e("Backup", "Error al subir backup", it)
                return@addOnFailureListener
            }

        return true
    }

}