package org.itb.sga.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parametro_reporte")
data class ParametroReporte(
    @PrimaryKey val id: Int,
    val name: String
)