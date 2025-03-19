package org.itb.sga.core

import com.preat.peekaboo.image.picker.ResizeOptions

const val SERVER_URL = "https://sga.itb.edu.ec/"
//const val SERVER_URL = "http://10.0.2.2:8000/"

val ROUTES = setOf(
    "login", "home", "inscripciones", "account", "alu_finanzas",
    "alu_cronograma", "alu_malla", "alu_horarios", "online",
    "alu_materias", "alu_facturacion_electronica", "alu_notas",
    "docentes", "pro_clases", "pro_horarios", "solicitudonline", "addSolicitud",
    "admin_ayudafinanciera", "documentos_alu", "documentos", "beca_solicitud",
    "alumnos_cab", "consultaalumno", "alu_matricula", "pro_cronograma",
    "reportes", "pro_evaluaciones", "pro_entrega_acta"
)

val resizeOptions = ResizeOptions(
    width = 1200, // Custom width
    height = 1200, // Custom height
    resizeThresholdBytes = 2 * 1024 * 1024L, // Custom threshold for 2MB,
    compressionQuality = 0.5 // Adjust compression quality (0.0 to 1.0)
)