package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.itb.sga.data.network.models.Canton
import org.itb.sga.data.network.models.EstadoCivil

@Serializable
data class FichaDatosPersonalesData(
    val calleUrbanizacion: String?,
    val celular: String?,
    val ciudadNacimiento: String?,
    val ciudadResidencia: Canton?,
    val convencional: String?,
    val edad: Int?,
    val emailInst: String?,
    val emailPersonal: String?,
    val estadoCivil: EstadoCivil?,
    val fechaNacimiento: String?,
    val identificacion: String?,
    val nacionalidad: String?,
    val nombre: String?,
    val numeroCasa: String?,
    val sectorResidencia: SectorResidencia?,
    val tipoIdentificacion: String?,

    val action: String?,
    val idInscripcion: Int?
)