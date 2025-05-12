package com.example.dam_proyecto_pablo_carbonero.lib.repositories.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.repositories.TuningRepository
import javax.inject.Inject

class TuningRepositoryImpl @Inject constructor(
    val tuningDao: TuningDao
) : TuningRepository {
    override suspend fun getAllTunings(): List<Tuning> {
        return tuningDao.getAllTunings()
    }

    override suspend fun getTuningById(id: Long): Tuning {
        return tuningDao.getTuningById(id)
    }
    override suspend fun insertTuning(tuning: Tuning): Long {
        return tuningDao.insertTuning(tuning)
    }

    override suspend fun deleteAllTunings(): Int {
        return tuningDao.deleteAllTunings()
    }

    override suspend fun deleteTuningById(id: Long): Int {
        return tuningDao.deleteTuningById(id)
    }

    override suspend fun updateTuningName(tuningId: Long, newName: String) {
        return tuningDao.updateTuningName(tuningId, newName)
    }
}