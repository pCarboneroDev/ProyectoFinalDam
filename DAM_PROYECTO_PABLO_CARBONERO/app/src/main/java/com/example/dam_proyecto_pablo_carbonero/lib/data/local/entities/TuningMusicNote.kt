package com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    //primaryKeys = ["tuningId", "noteId"],
    foreignKeys = [
        ForeignKey(entity = Tuning::class, parentColumns = ["id"], childColumns = ["tuningId"]),
        ForeignKey(entity = MusicNote::class, parentColumns = ["id"], childColumns = ["noteId"])
    ]
)
data class TuningMusicNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tuningId: Long,
    val noteId: Long
)

