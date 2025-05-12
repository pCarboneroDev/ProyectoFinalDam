package com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tunings")
data class Tuning(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String
)
