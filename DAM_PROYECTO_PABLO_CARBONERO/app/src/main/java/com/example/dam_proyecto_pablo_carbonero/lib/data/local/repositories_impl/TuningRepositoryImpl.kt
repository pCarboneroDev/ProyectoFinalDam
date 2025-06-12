package com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl

import com.example.dam_proyecto_pablo_carbonero.lib.data.local.dao.TuningDao
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.repositories.TuningRepository
import javax.inject.Inject

class TuningRepositoryImpl @Inject constructor(
    val tuningDao: TuningDao
) : TuningRepository {

    /**
     * accede a tuningDao para obtener todas las afinacioes
     * @return Lista Tuning
     */
    override suspend fun getAllTunings(): List<Tuning> {
        return tuningDao.getAllTunings()
    }

    /**
     * accede a tuningDao para obtener una afinación por id
     * @param id
     * @return Tuning
     */
    override suspend fun getTuningById(id: Long): Tuning {
        return tuningDao.getTuningById(id)
    }

    /**
     * accede a tuningDao para insertar una afinación
     * @param tuning
     * @return id
     */
    override suspend fun insertTuning(tuning: Tuning): Long {
        return tuningDao.insertTuning(tuning)
    }

    /**
     * accede a tuningDao para borrar todas las afinaciones
     * @return filas afectadas
     */
    override suspend fun deleteAllTunings(): Int {
        return tuningDao.deleteAllTunings()
    }

    /**
     * accede a tuningDao para borrar una afinación por id
     * @param id long
     * @return filas afectadas
     */
    override suspend fun deleteTuningById(id: Long): Int {
        return tuningDao.deleteTuningById(id)
    }

    /**
     * accede a tuningDao para actualiar el nombre de una afinación
     * @param tuningId
     * @param newName
     */
    override suspend fun updateTuningName(tuningId: Long, newName: String) {
        return tuningDao.updateTuningName(tuningId, newName)
    }

    /**
     * accede a tuningDao para cambiar el estado de favorito de una afinacion
     * @param tuningId
     * @param favourite
     */
    override suspend fun updateTuningFavourite(tuningId: Long, favourite: Boolean) {
        return tuningDao.updateTuningFavourite(tuningId, favourite)
    }

    /**
     * accede a tuningDao para insertar una lsita de afinaciones
     * @param list
     * @return lista ids
     */
    override suspend fun insertAllTuning(list: List<Tuning>): List<Long> {
        return tuningDao.insertAllTuning(list)
    }
}