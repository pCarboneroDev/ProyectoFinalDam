package com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Songs",
    foreignKeys = [
        ForeignKey(
            entity = Tuning::class,
            parentColumns = ["id"],
            childColumns = ["tuningId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Song(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String,
    val bandName: String,
    val tuningId: Long,
    val bpm: String,
    val tabs: String
)
