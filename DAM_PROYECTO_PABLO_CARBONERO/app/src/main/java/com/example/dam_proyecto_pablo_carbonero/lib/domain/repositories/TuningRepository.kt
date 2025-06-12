package com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning

/**
 * Interfaz que define todas las posibles acciones que se pueden realizar con las afinaciones
 */
interface TuningRepository {
    suspend fun getAllTunings():List<Tuning>

    suspend fun getTuningById(id: Long): Tuning

    suspend fun insertTuning(tuning: Tuning): Long

    suspend fun deleteAllTunings(): Int

    suspend fun deleteTuningById(id: Long): Int

    suspend fun updateTuningName(tuningId: Long, newName: String)

    suspend fun updateTuningFavourite(tuningId: Long, favourite: Boolean)

    suspend fun insertAllTuning(list: List<Tuning>): List<Long>
}