package org.example.aok.features.student.alu_documentos

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mohamedrejeb.calf.picker.toImageBitmap
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.example.aok.core.SERVER_URL
import org.example.aok.core.getChipState
import org.example.aok.core.resizeOptions
import org.example.aok.data.network.AluDocumento
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.alerts.MyConfirmAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

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
                aluDocumentosViewModel,
                navController
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
    navController: NavHostController
) {
    val data by aluDocumentosViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

    LaunchedEffect(Unit) {
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
    val scope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var mensaje by remember { mutableStateOf<String>("") }

    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = resizeOptions,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                imageBitmap = it.toImageBitmap()
                imageByteArray = it
            }
        }
    )

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
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(imageVector = Icons.Filled.UploadFile, contentDescription = "Subir archivo", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                } else {
                    if (!documento.verificado) {
                        IconButton(
                            onClick = {
                                aluDocumentosViewModel.changeDocumentSelect(documento)
                                singleImagePicker.launch()
                                mensaje = "Va a modificar el archivo para el documento: ${documento.nombreDocumento}."
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.secondary
                            ),
                        ) {
                            Icon(imageVector = Icons.Filled.UploadFile, contentDescription = "Modificar archivo")
                        }

                        IconButton(
                            onClick = {  },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar archivo")
                        }
                    }

                    IconButton(
                        onClick = { homeViewModel.openURL("${SERVER_URL}media/${documento.archivo}") },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
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

    if (imageByteArray != null) {
        MyConfirmAlert(
            titulo = "¿Desea continuar?",
            mensaje = mensaje,
            onCancel = {
                imageBitmap = null
                imageByteArray = null
                mensaje = ""
                aluDocumentosViewModel.clearDocunentSelect()
            },
            onConfirm = {

            },
            showAlert = true
        )
    }


}
