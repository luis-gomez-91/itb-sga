package org.itb.sga.features.teacher.pro_entrega_actas

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinx.coroutines.launch
import org.itb.sga.core.SERVER_URL
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActas
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActasDocente
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextFieldArea
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.alerts.MyInfoAlert
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
                homeViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    homeViewModel: HomeViewModel
) {
    val data by proEntregaActasViewModel.data.collectAsState(emptyList())
    val periodo by homeViewModel.periodoSelect.collectAsState(null)
    val error by proEntregaActasViewModel.error.collectAsState(null)
    val response by proEntregaActasViewModel.response.collectAsState(null)
    val showBottomSheet by proEntregaActasViewModel.showBottomSheet.collectAsState(false)
    val loading by proEntregaActasViewModel.isLoading.collectAsState(false)

    LaunchedEffect(periodo) {
        homeViewModel.homeData.value?.persona?.idDocente?.let {
            proEntregaActasViewModel.onloadProEntregaActas(
                it, homeViewModel
            )
        }
    }

    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
        }
    }

    Column (
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        periodo?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    text = it.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }

            when {
                data.isEmpty() -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No se encontraron materias para el periodo seleccionado.",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(data) { materia ->
                            MateriaItem(proEntregaActasViewModel, materia, homeViewModel)
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }

        } ?: run {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Seleccione un periodo.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
    error?.let{
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                proEntregaActasViewModel.clearError()
            },
            showAlert = true
        )
    }

    response?.let {
        MyInfoAlert(
            titulo = it.status,
            mensaje = it.message,
            onDismiss = {
                proEntregaActasViewModel.clearResponse()
            },
            showAlert = true
        )
    }

    if (showBottomSheet) {
        EntregarActaNotasForm(proEntregaActasViewModel, homeViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntregarActaNotasForm (
    proEntregaActasViewModel: ProEntregaActasViewModel,
    homeViewModel: HomeViewModel
) {
    val materiaSelect by proEntregaActasViewModel.materiaSelect.collectAsState(null)
    var observaciones by remember { mutableStateOf("") }
    var actaName by remember { mutableStateOf("Archivo no seleccionado") }
    var informeName by remember { mutableStateOf("Archivo no seleccionado") }

    var fileActa: ByteArray? by remember { mutableStateOf(null) }
    var fileInform: ByteArray? by remember { mutableStateOf(null) }

    ModalBottomSheet(
        onDismissRequest = {
            proEntregaActasViewModel.updateShowBottomSheet(false)
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Entregar acta de calificaciones",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            materiaSelect?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.asignatura,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            FilePickerRow(
                label = "Acta:",
                fileName = actaName,
                onFileSelected = { file, name ->
                    fileActa = file
                    actaName = name
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            FilePickerRow(
                label = "Resumen de acta:",
                fileName = informeName,
                onFileSelected = { file, name ->
                    fileInform = file
                    informeName = name
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Observaciones:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )


            MyOutlinedTextFieldArea(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = "Observaciones",
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "Enviar",
                    buttonColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.AutoMirrored.Filled.Send,
                    onClickAction = {
                        homeViewModel.homeData.value?.persona?.idDocente?.let {
                            val formValido = validateForm(fileActa, fileInform, observaciones, it, proEntregaActasViewModel)

                            if (formValido) {
                                proEntregaActasViewModel.updateShowBottomSheet(false)
                                proEntregaActasViewModel.updateLoading(true)
                                fileActa?.let { it1 ->
                                    fileInform?.let { it2 ->
                                        proEntregaActasViewModel.entregarActa(
                                            fileActa = it1,
                                            nameActa = actaName,
                                            fileInforme = it2,
                                            nameInforme = informeName,
                                            idProfesor = it,
                                            observaciones = observaciones
                                        )
                                    }
                                }
                            }
                        }
                    },
                    iconSize = 24.dp,
                    textStyle = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

fun validateForm(
    fileActa: ByteArray?,
    fileInform: ByteArray?,
    observaciones: String,
    idProfesor: Int?,
    proEntregaActasViewModel: ProEntregaActasViewModel
): Boolean {
    val message = when {
        fileActa == null -> "Debe seleccionar un archivo de Acta."
        fileInform == null -> "Debe seleccionar un archivo de Resumen de Acta."
        observaciones.isBlank() -> "Debe ingresar observaciones."
        idProfesor == null -> "No se encontró un ID de profesor válido."
        else -> null
    }

    message?.let {
        proEntregaActasViewModel.addError(Error("Faltan datos", it))
        return false
    }
    return true
}

@Composable
fun FilePickerRow(
    label: String,
    fileName: String,
    onFileSelected: (ByteArray, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
    )
    Spacer(modifier = Modifier.height(4.dp))

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color.Transparent,
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyFilledTonalButton(
                text = "Seleccionar archivo",
                buttonColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.secondary,
                icon = Icons.Filled.PictureAsPdf,
                onClickAction = {
                    coroutineScope.launch {
                        val file = FileKit.pickFile()
                        file?.let {
                            onFileSelected(it.readBytes(), it.name)
                        }
                    }
                },
                iconSize = 16.dp,
                textStyle = MaterialTheme.typography.labelSmall,
                shape = RoundedCornerShape(4.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = fileName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}


@Composable
fun MateriaItem(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    materia: ProEntregaActas,
    homeViewModel: HomeViewModel
) {


    var expanded by remember { mutableStateOf(false) }
    var showActions by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = materia.asignatura,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
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
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatoText("Grupo:", "${materia.grupo} (${materia.nivelMalla})"),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatoText("Fecha:", "${materia.desde} a ${materia.hasta}"),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatoText("Carrera:", materia.carrera),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

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

            IconButton(
                onClick = {
                    showActions = !showActions
                    proEntregaActasViewModel.updateMateriaSelect(materia)
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
                        onDismissRequest = {
                            showActions = false
                            proEntregaActasViewModel.updateMateriaSelect(null)
                        },
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownActions(
    proEntregaActasViewModel: ProEntregaActasViewModel,
    materia: ProEntregaActas,
    onDismissRequest: () -> Unit,
    homeViewModel: HomeViewModel
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
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                if (materia.nivelCerrado) {
                                    proEntregaActasViewModel.addError(
                                        Error(
                                            title = "Acción no permitida",
                                            error = "El nivel está cerrado. Esta acción solo se puede realizar desde la plataforma web."
                                        )
                                    )
                                } else {
                                    proEntregaActasViewModel.updateShowBottomSheet(true)

                                }
                            }
                    ){
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.Description,
                            contentDescription = "Entrega de acta",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Entregar acta de calificaciones",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    materia.actaFile?.let {
                        Spacer(Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    homeViewModel.openURL("${SERVER_URL}media/$it")
                                }
                        ){
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = "Descargar acta",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Descargar acta",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }

                    materia.informeFile?.let {
                        Spacer(Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    homeViewModel.openURL("${SERVER_URL}media/$it")
                                }
                        ){
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = "Descargar informe",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Descargar informe",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
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
                text = formatoText("Fecha:", "${docente.fechaDesde} a ${docente.fechaHasta}"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatoText("Segmento:", docente.segmento),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

