package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getPath
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.example.aok.data.domain.DrawerItem
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.common.login.RedesSociales

@Composable
fun MyDrawerContent(
    drawerState: DrawerState,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
//    val homeDataState = homeViewModel.homeData.observeAsState()
//    val persona = homeDataState.value?.persona
//
    val items = listOf(
        DrawerItem("Perfil", Icons.Filled.Person, "account"),
        DrawerItem("Cambiar contraseña", Icons.Filled.LockPerson, ""),
        DrawerItem("Cerrar sesión", Icons.Filled.Logout, "logout")
    )

    val imageLogo =
        if (isSystemInDarkTheme()) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }

//    val defaultPainter: Painter = painterResource(id = R.drawable.logo)

    val scopee = rememberCoroutineScope()
    val context = LocalPlatformContext.current
    var byteArray by remember { mutableStateOf(ByteArray(0)) }
    var platformSpecificFilePath by remember { mutableStateOf("") }
    var platformSpecificFile by remember { mutableStateOf<KmpFile?>(null) }


    val pickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Multiple,
        onResult = { files ->
            scopee.launch {
                files.firstOrNull()?.let {
//                    byteArray = it.readByteArray(contextt)
                    platformSpecificFile = it
//                    platformSpecificFilePath = it.getPath(context) ?: ""
                }
            }
        }
    )

    ModalDrawerSheet(

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Sistema de Gestión Académica",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "INSTITUTO TECNOLÓGICO BOLIVARIANO DE TECNOLOGÍA",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Image(
//                        painter = painterResource(imageLogo),
//                        contentDescription = "logo",
//                        modifier = Modifier.fillMaxWidth(0.8f)
//                    )


                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        AsyncImage(
//                            model = loginViewModel.userData.value!!.photo,
                            model = homeViewModel.homeData.value?.persona?.foto,
                            contentDescription = "Foto perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )

                        IconButton(
                            onClick = {
                                pickerLauncher.launch()
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(48.dp)
                                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), shape = CircleShape)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                modifier = Modifier.size(40.dp).padding(4.dp),
                                imageVector = Icons.Filled.Camera,
                                contentDescription = "Actualizar foto",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }

                Text(
                    text = homeViewModel.homeData.value?.persona?.nombre ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "(${homeViewModel.homeData.value?.persona?.identificacion})" ?: "",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label, fontSize = 16.sp) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            if (item.label == "Cerrar sesión") {
                                loginViewModel.onLogout(navHostController)
                            } else {
                                navHostController.navigate(item.navigate)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }

            RedesSociales(
                modifier = Modifier,
                homeViewModel = homeViewModel
            )
        }
    }
}
