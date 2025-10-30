package org.itb.sga.core

import com.preat.peekaboo.image.picker.ResizeOptions
import org.itb.sga.data.network.reportes.DjangoModelItem

const val DATABASE_NAME = "aok.db"

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

val LIST_SEXO = listOf(
    DjangoModelItem(1, "FEMENINO"),
    DjangoModelItem(2, "MASCULINO") 
)

val LIST_SANGRE = listOf(
    DjangoModelItem(1, "O-"),
    DjangoModelItem(2, "O+"),
    DjangoModelItem(3, "A-"),
    DjangoModelItem(4, "A+"),
    DjangoModelItem(5, "B-"),
    DjangoModelItem(6, "B+"),
    DjangoModelItem(7, "AB-"),
    DjangoModelItem(8, "AB+")
)

val LIST_ESTADO_CIVIL = listOf(
    DjangoModelItem(1, "SOLTERO"),
    DjangoModelItem(2, "CASADO"),
    DjangoModelItem(3, "DIVORCIADO"),
    DjangoModelItem(4, "VIUDO"),
    DjangoModelItem(5, "UNION DE HECHO"),
    DjangoModelItem(6, "SEPARADO"),
)

val LIST_ETNIA = listOf(
    DjangoModelItem(1, "AFROECUATORIANO O AFRODESCENDIENTE"),
    DjangoModelItem(2, "INDIGENA"),
    DjangoModelItem(3, "BLANCO"),
    DjangoModelItem(4, "MESTIZO"),
    DjangoModelItem(5, "NEGRO"),
    DjangoModelItem(6, "MULATO"),
    DjangoModelItem(7, "OTRO"),
    DjangoModelItem(8, "MONTUBIO")
)