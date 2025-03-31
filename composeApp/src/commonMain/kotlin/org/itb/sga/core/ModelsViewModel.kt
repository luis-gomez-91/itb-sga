package org.itb.sga.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.itb.sga.data.domain.TipoEstado

class ModelsViewModel : ViewModel() {
    val tiposEstados: List<String> = listOf("APROBADO", "REPROBADO", "EN CURSO", "RECUPERACION", "EXAMEN")

    private val _tipoEstado = MutableStateFlow(
        tiposEstados.associateWith { tipo ->
            TipoEstado(
                nombre = tipo,
                colocarExamen = tipo == "RECUPERACION" || tipo == "EXAMEN"
            )
        }
    )

    val tipoEstado: StateFlow<Map<String, TipoEstado>> = _tipoEstado



}


