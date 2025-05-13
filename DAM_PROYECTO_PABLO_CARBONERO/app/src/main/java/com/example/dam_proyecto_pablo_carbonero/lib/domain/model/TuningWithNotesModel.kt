package com.example.dam_proyecto_pablo_carbonero.lib.domain.model

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning

data class TuningWithNotesModel(
    val tuning: Tuning,
    val noteList: List<MusicNote>
) {}