package com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.firebaseUseCases

import android.util.Log
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.MusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.SongDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningMusicNoteDao
import com.example.dam_proyecto_pablo_carbonero.lib.domain.usecases.UseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class CreateBackupUseCase @Inject constructor(
    private val musicNoteDao: MusicNoteDao,
    private val tuningDao: TuningDao,
    private val tuningMusicNoteDao: TuningMusicNoteDao,
    private val songDao: SongDao
): UseCase<Unit, Boolean> {
    override suspend fun call(param: Unit): Boolean {
        val notes = musicNoteDao.getAllNotes()
        val tunings = tuningDao.getAllTunings()
        val tuningMusicNote = tuningMusicNoteDao.getAllTuningMusicNote()
        val songs = songDao.getAllSongs()

        val notesMap = mapOf(
            "MusicNote" to notes, //Gson().toJson(notes),
            "Tuning" to tunings,//Gson().toJson(tunings),
            "TuningMusicNote" to tuningMusicNote, //Gson().toJson(tuningMusicNote),
            "Song" to songs, //Gson().toJson(songs),
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