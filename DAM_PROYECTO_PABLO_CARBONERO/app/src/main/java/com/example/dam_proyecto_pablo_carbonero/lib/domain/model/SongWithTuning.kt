package com.example.dam_proyecto_pablo_carbonero.lib.domain.model

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning

data class SongWithTuning(
    val tuning: Tuning,
    val song: Song,
    val noteList: List<MusicNote> = emptyList<MusicNote>()
) {}