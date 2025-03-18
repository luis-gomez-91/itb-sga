package org.example.aok.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.aok.data.entity.ParametroReporte

@Dao
interface ParametroReporteDao {
    @Upsert
    suspend fun upsert(parametroReporte: ParametroReporte)

    @Query("SELECT * FROM parametro_reporte WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ParametroReporte?

    @Query("SELECT * FROM parametro_reporte")
    fun getAll(): Flow<List<ParametroReporte>>

    @Query("DELETE FROM parametro_reporte")
    suspend fun delete()
}