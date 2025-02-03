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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.picker.toImageBitmap
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.aok.core.SERVER_URL
import org.example.aok.core.logInfo
import org.example.aok.core.resizeOptions
import org.example.aok.data.domain.DrawerItem
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField
import org.example.aok.ui.components.SocialMedia

@Composable
fun MyDrawerContent(
    drawerState: DrawerState,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
    val showPasswordForm by homeViewModel.showPasswordForm.collectAsState(false)

    val items = listOf(
        DrawerItem("Perfil", Icons.Filled.Person, { navHostController.navigate("account") }),
        DrawerItem("Cambiar contraseña", Icons.Filled.LockPerson, { homeViewModel.changeShowPasswordForm(true) }),
        DrawerItem("Cerrar sesión", Icons.Filled.Logout, { loginViewModel.onLogout(navHostController) })
    )

    val imageLogo =
        if (isSystemInDarkTheme()) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }

    ModalDrawerSheet(

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
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

                PhotoProfile(homeViewModel, scope)

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
                            item.onclick()
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

            SocialMedia(
                modifier = Modifier,
                homeViewModel = homeViewModel
            )
        }
    }

    if (showPasswordForm) {
        ChangePasswordForm(homeViewModel)
    }
}

@Composable
fun PhotoProfile(
    homeViewModel: HomeViewModel,
    scope: CoroutineScope
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    val imageLoading by homeViewModel.imageLoading.collectAsState(false)
    val photoUploaded by homeViewModel.photoUploaded.collectAsState()


    LaunchedEffect(photoUploaded) {
        if (photoUploaded) {
            imageBitmap = null // Limpia la imagen al completarse la subida
            homeViewModel.resetPhotoUploadedFlag() // Restablece el estado en el ViewModel
        }
    }

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f)
        ) {
            if (imageLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 4.dp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = "Foto perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )

                    IconButton(
                        onClick = {
                            scope.launch {
                                imageByteArray?.let { homeViewModel.uploadPhoto(it) }

                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(48.dp)
                            .background(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f), shape = CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp).padding(4.dp),
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Guardar foto",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(
                        onClick = {
                            imageBitmap = null
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(48.dp)
                            .background(color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f), shape = CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp).padding(4.dp),
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Guardar foto",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    logInfo("prueba", "${SERVER_URL}${homeViewModel.homeData.value?.persona?.foto}")
                    AsyncImage(
                        model = "${SERVER_URL}${homeViewModel.homeData.value?.persona?.foto}",
                        contentDescription = "Foto perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )

                    IconButton(
                        onClick = {
                            singleImagePicker.launch()
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordForm(
    homeViewModel: HomeViewModel
) {
    val previousPassword by homeViewModel.previousPassword.collectAsState("")
    val newPassword1 by homeViewModel.newPassword1.collectAsState("")
    val newPassword2 by homeViewModel.newPassword2.collectAsState("")
    val isFormValid by homeViewModel.isFormValid.collectAsState(false)

    ModalBottomSheet(
        onDismissRequest = {
            homeViewModel.changeBottomSheet()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Cambiar contraseña",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyOutlinedTextField(
                value = previousPassword,
                onValueChange = { homeViewModel.onPasswordChange(it, newPassword1, newPassword2) },
                placeholder = "Contraseña actual",
                label = "Contraseña actual",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            MyOutlinedTextField(
                value = newPassword1,
                onValueChange = { homeViewModel.onPasswordChange(previousPassword, it, newPassword2) },
                placeholder = "Nueva contraseña",
                label = "Nueva contraseña",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            MyOutlinedTextField(
                value = newPassword2,
                onValueChange = { homeViewModel.onPasswordChange(previousPassword, newPassword1, it) },
                placeholder = "Nueva contraseña",
                label = "Nueva contraseña",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "Enviar",
                    enabled = isFormValid,
                    icon = Icons.Filled.Save,
                    iconSize = 20.dp,
                    onClickAction = {
                        homeViewModel.requestChangePassword()
                    }
                )
            }
        }
    }
}