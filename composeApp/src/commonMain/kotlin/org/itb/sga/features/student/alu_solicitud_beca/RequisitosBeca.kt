package org.itb.sga.features.student.alu_solicitud_beca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.alerts.MyAlert

@Composable
fun RequisitosBeca(
    showAlert: Boolean,
    onDismiss: () -> Unit,
    homeViewModel: HomeViewModel
) {
    MyAlert(
        title = "Requisitos Beca",
        onDismiss = onDismiss,
        showAlert = showAlert,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyFilledTonalButton(
                    text = "Descargar requisitos",
                    enabled = true,
                    onClickAction = {
                        homeViewModel.openURL("https://www.itb.edu.ec/public/docs/requisitos_para_beca.pdf")
                    },
                    buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Filled.Download
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Sección 1: Cumplimiento Académico
                    item {
                        Text(
                            text = "1. Cumplimiento Académico",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Los estudiantes deben haber finalizado el primer nivel de estudios con calificaciones en todas las asignaturas y un promedio mínimo de 90 puntos. " +
                                    "Estudiantes con carnet de discapacidad aplican con un promedio mínimo de 80 puntos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Sección 2: Matrícula
                    item {
                        Text(
                            text = "2. Matrícula",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "El estudiante debe estar matriculado en el nuevo nivel de estudio y no tener valores pendientes de pago. Si necesita matricularse, puede hacerlo en el Sistema de Gestión Académica (SGA), módulo Matrícula en Línea.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Sección 3: Actualización de Datos
                    item {
                        Text(
                            text = "3. Actualización de Datos",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Es necesario revisar y actualizar todos los datos personales en el SGA.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Sección 4: Ficha socioeconómica
                    item {
                        Text(
                            text = "4. Ficha socioeconómica",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Llenar la Ficha socioeconómica en el SGA con información estrictamente apegada a la verdad.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Sección 5: Solicitud de Beca
                    item {
                        Text(
                            text = "5. Solicitud de Beca",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Acceder al SGA, módulo SOLICITUD BECA, y completar el proceso de llenado con información estrictamente verídica. " +
                                    "En caso de no poder acceder al módulo, enviar una solicitud tipo \"REQUERIMIENTO A TICS\" con la descripción \"NO PUEDO INGRESAR A SGA O A SOLICITUD DE BECA\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Documentos requeridos
                    item {
                        Text(
                            text = "Documentos Requeridos",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = """
                    - Cédula ambos lados en formato PDF.
                    - Carnet de discapacidad en formato PDF (si aplica).
                    - Cédula o partida de nacimiento en formato PDF (si tiene hijos).
                    - Rol de pago o certificado laboral que indique el sueldo de quien cubre los estudios, en formato PDF.
                """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Nota importante
                    item {
                        Text(
                            text = "Importante",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Las becas se otorgan considerando el promedio y el estudio socioeconómico del estudiante. Los requisitos deben subirse al sistema antes de la fecha de vencimiento de la cuota #1 del nivel actual.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
    )
}