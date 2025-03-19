package org.itb.sga.features.student.alu_documentos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinx.coroutines.launch
import org.itb.sga.core.SERVER_URL
import org.itb.sga.core.getChipState
import org.itb.sga.data.network.AluDocumento
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyConfirmAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluDocumentosScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluDocumentosViewModel: AluDocumentosViewModel
) {
    DashBoardScreen(
        title = "Mis Documentos",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluDocumentosViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluDocumentosViewModel: AluDocumentosViewModel,
) {
    val data by aluDocumentosViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val response by aluDocumentosViewModel.response.collectAsState(null)

    LaunchedEffect(response) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluDocumentosViewModel.onloadAluDcoumentos(
                it, homeViewModel)
        }
    }

    val filterData = if (searchQuery.isNotEmpty()) {
        data.filter { it.nombreDocumento.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filterData) { documento ->
                    DocumentoItem(
                        documento = documento,
                        homeViewModel = homeViewModel,
                        aluDocumentosViewModel = aluDocumentosViewModel
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DocumentoItem(
    documento: AluDocumento,
    homeViewModel: HomeViewModel,
    aluDocumentosViewModel: AluDocumentosViewModel
) {

    val documentSelect by aluDocumentosViewModel.documentSelect.collectAsState(null)
    val action by aluDocumentosViewModel.action.collectAsState(null)
    val scope = rememberCoroutineScope()
    var mensaje by remember { mutableStateOf<String>("") }

    MyCard (
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = documento.nombreDocumento,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            val chipState = getChipState(documento.verificado)
            MyAssistChip(
                label = chipState.label,
                containerColor = chipState.containerColor,
                labelColor = chipState.labelColor,
                icon = chipState.icon
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (documento.archivo.isNullOrBlank()) {
                    mensaje = UploadFile(
                        aluDocumentosViewModel = aluDocumentosViewModel,
                        documento = documento
                    )
                } else {
                    if (!documento.verificado) {
                        mensaje = UploadFile(
                            aluDocumentosViewModel = aluDocumentosViewModel,
                            documento = documento
                        )

                        IconButton(
                            onClick = {
                                mensaje = "Va a eliminar documento: ${documento.nombreDocumento}"
                                aluDocumentosViewModel.changeDocumentSelect(documento)
                                aluDocumentosViewModel.changeAction("removeDocument")
                            },
                            colors = IconButtonDefaults.iconButtonColors(
//                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar archivo")
                        }
                    }

                    IconButton(
                        onClick = { homeViewModel.openURL("${SERVER_URL}media/${documento.archivo}") },
                        colors = IconButtonDefaults.iconButtonColors(
//                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Ver archivo",
                        )
                    }
                }
            }
        }
    }

    if (documentSelect != null) {
        MyConfirmAlert(
            titulo = "¿Desea continuar?",
            mensaje = mensaje,
            onCancel = {
                aluDocumentosViewModel.changeFile(null)
                mensaje = ""
                aluDocumentosViewModel.changeDocumentSelect(null)
            },
            onConfirm = {
                scope.launch {
                    when (action) {
                        "removeDocument" -> aluDocumentosViewModel.removeDocument(homeViewModel.homeData.value!!.persona.idPersona)
                        "sendDocument" -> aluDocumentosViewModel.sendDocument(homeViewModel.homeData.value!!.persona.idPersona)
                    }
                }
            },
            showAlert = true
        )
    }
}

@Composable
fun UploadFile(
    aluDocumentosViewModel: AluDocumentosViewModel,
    documento: AluDocumento
) :String {
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val fileName by aluDocumentosViewModel.fileName.collectAsState(null)
    IconButton(
        onClick = {
            aluDocumentosViewModel.changeDocumentSelect(documento)
            aluDocumentosViewModel.changeAction("sendDocument")
            scope.launch {
                try {
                    val file = FileKit.pickFile()
                    if (file != null) {
                        aluDocumentosViewModel.changeFile(file.readBytes())
                        aluDocumentosViewModel.changeFileName(file.name)
                    } else {
                        errorMessage = "No se seleccionó ningún archivo."
                    }
                } catch (e: Exception) {
                    errorMessage = "Error al seleccionar archivo: ${e.message}"
                }
            }
        }
    ) {
        Icon(imageVector = Icons.Filled.UploadFile, contentDescription = "Subir archivo", tint = MaterialTheme.colorScheme.onPrimary)
    }
    return "Archivo seleccionado: ${fileName}"
}
