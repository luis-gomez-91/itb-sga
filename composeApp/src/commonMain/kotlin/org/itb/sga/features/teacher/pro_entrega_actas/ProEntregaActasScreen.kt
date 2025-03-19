package org.itb.sga.features.teacher.pro_entrega_actas

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActas
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActasDocente
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ProEntregaActasScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proEntregaActasViewModel: ProEntregaActasViewModel
) {
    DashBoardScreen(
        title = "Entrega de Actas",
        navController = navController,
        content = {
            Screen(
                proEntregaActasViewModel,
                homeViewModel,
                navController
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    val data by proEntregaActasViewModel.data.collectAsState(emptyList())
    val periodo by homeViewModel.periodoSelect.collectAsState(null)
    val error by proEntregaActasViewModel.error.collectAsState(null)

    LaunchedEffect(periodo) {
        homeViewModel.homeData.value?.persona?.idDocente?.let {
            proEntregaActasViewModel.onloadProEntregaActas(
                it, homeViewModel
            )
        }
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(data) { materia ->
            MateriaItem(proEntregaActasViewModel, materia, navController)
            HorizontalDivider()
        }
    }
    if (error != null) {
        MyErrorAlert(
            titulo = error!!.title,
            mensaje = error!!.error,
            onDismiss = {
                proEntregaActasViewModel.clearError()
            },
            showAlert = true
        )
    }
}

@Composable
fun MateriaItem(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    materia: ProEntregaActas,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    var showActions by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = materia.asignatura,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(8.dp))
            AnimatedContent(targetState = expanded) { isExpanded ->
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column (
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Text(
                    text = formatoText("Grupo: ", "${materia.grupo} (${materia.nivelMalla})"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatoText("Carrera: ", materia.carrera),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatoText("Fecha: ", "${materia.desde} a ${materia.hasta}"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyAssistChip(
                        label = if (materia.nivelCerrado) "Nivel cerrado" else "Nivel abierto",
                        containerColor = if(materia.nivelCerrado) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        labelColor = if(materia.nivelCerrado) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(4.dp))
                    MyAssistChip(
                        label = "Estado: ${materia.estado}",
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                    )
                }


                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Docentes asignados",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Spacer(Modifier.height(4.dp))
                        LazyRow (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(materia.docentes) { docente ->
                                DocenteItem(docente)
                                Spacer(Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.width(4.dp))
            Column (
                modifier = Modifier
            ) {
                IconButton(
                    onClick = {
                        showActions = !showActions
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More Information",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Box {
                    if (showActions) {
                        DropdownActions(
                            proEntregaActasViewModel = proEntregaActasViewModel,
                            materia = materia,
                            onDismissRequest = { showActions = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownActions(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    materia: ProEntregaActas,
    onDismissRequest: () -> Unit
) {
    Popup (
        alignment = Alignment.TopStart,
        properties = PopupProperties(),
        onDismissRequest = onDismissRequest
    ){
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            if (materia.nivelCerrado) {
                                proEntregaActasViewModel.addError(
                                    Error(
                                        title = "Acción no permitida",
                                        error = "El nivel está cerrado. Esta acción solo se puede realizar desde la plataforma web."
                                    )
                                )
                            } else {
                                proEntregaActasViewModel.entregarActa(materia)
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.Description,
                            contentDescription = "Entrega de acta",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Entregar Acta",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocenteItem(
    docente: ProEntregaActasDocente
) {
    MyCard {
        Column {
            Text(
                text = docente.docente,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = formatoText("Fecha: ", "${docente.fechaDesde} a ${docente.fechaHasta}"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatoText("Segmento: ", docente.segmento),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

