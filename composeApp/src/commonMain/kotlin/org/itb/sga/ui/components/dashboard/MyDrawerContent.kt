package org.itb.sga.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.picker.toImageBitmap
import compose.icons.TablerIcons
import compose.icons.tablericons.Archive
import compose.icons.tablericons.Check
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.DeviceMobile
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Logout
import compose.icons.tablericons.Moon
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Sun
import compose.icons.tablericons.User
import compose.icons.tablericons.X
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.domain.DrawerItem
import org.itb.sga.data.network.Home
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.SocialMedia


@Composable
fun MyDrawerContent(
    drawerState: DrawerState,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
    val showPasswordForm by homeViewModel.showPasswordForm.collectAsState(false)
    val homeData by homeViewModel.homeData.collectAsState(null)
    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)

    var themeIcon by remember { mutableStateOf(TablerIcons.Sun) }

    LaunchedEffect(selectedTheme) {
        themeIcon = if (selectedTheme?.dark == true) TablerIcons.Moon else TablerIcons.Sun
    }

    val items = buildList {
        add(DrawerItem("Perfil", TablerIcons.User) { navHostController.navigate("account") })
        add(DrawerItem("Cambiar contraseña", TablerIcons.Lock) {
            homeViewModel.changeShowPasswordForm(true)
        })
        if (homeData?.persona?.idInscripcion != null) {
            add(DrawerItem("Consulta general", TablerIcons.Archive) { navHostController.navigate("consultaalumno") })
        }
        add(DrawerItem("Tema", themeIcon) { homeViewModel.changeshowThemeSetting(true) })
//        add(DrawerItem("Opciones de biométrico", Icons.Filled.Fingerprint) { homeViewModel.changeshowThemeSetting(true) })
        add(DrawerItem("Cerrar sesión", TablerIcons.Logout) { loginViewModel.onLogout(navHostController, homeViewModel) })
    }


    ModalDrawerSheet(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceBright)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Sistema de Gestión Académica",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                homeData?.let { data ->
                    Spacer(Modifier.height(8.dp))
                    PhotoProfile(homeViewModel, scope, data)

                    data.persona.nacionalidadEmoticon?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = data.persona.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = data.persona.emailinst,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
//                    Text(
//                        text = data.persona.usuario,
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                items.forEach { item ->
                    Column {
                        NavigationDrawerItem(
                            label = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(24.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = item.label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Icon(
                                        imageVector = TablerIcons.ChevronRight,
                                        contentDescription = item.label,
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                item.onclick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    }
                }
            }

            SocialMedia(
                modifier = Modifier,
                homeViewModel = homeViewModel,
                size = 36.dp
            )
        }
    }

    if (showPasswordForm) {
        ChangePasswordForm(homeViewModel)
    }

    ThemeSettings(homeViewModel)
}

@Composable
fun PhotoProfile(
    homeViewModel: HomeViewModel,
    scope: CoroutineScope,
    homeData: Home?
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    val imageLoading by homeViewModel.imageLoading.collectAsState(false)
    val photoUploaded by homeViewModel.photoUploaded.collectAsState()

    LaunchedEffect(photoUploaded) {
        if (photoUploaded) {
            imageBitmap = null
            homeViewModel.resetPhotoUploadedFlag()
        }
    }

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
                                imageByteArray?.let {
                                    homeViewModel.uploadPhoto(it)
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Check,
                            contentDescription = "Guardar foto",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(
                        onClick = { imageBitmap = null },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = TablerIcons.X,
                            contentDescription = "Cancelar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    AsyncImage(
                        model = "${SERVER_URL}${homeData?.persona?.foto}",
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
                                val file = FileKit.openFilePicker(type = FileKitType.Image)

                                if (file != null) {
                                    val originalBytes = file.readBytes()

                                    val resizedBytes = FileKit.compressImage(
                                        bytes = originalBytes,
                                        quality = 80,
                                        maxWidth = 800,
                                        maxHeight = 800
                                    )

                                    imageByteArray = resizedBytes
                                    imageBitmap = resizedBytes.toImageBitmap()
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Photo,
                            contentDescription = "Actualizar foto",
                            tint = MaterialTheme.colorScheme.secondary
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
            homeViewModel.changeShowPasswordForm(false)
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Cambiar contraseña",
                style = MaterialTheme.typography.titleMedium,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettings(
    homeViewModel: HomeViewModel
) {
    val themeOptions by homeViewModel.requestThemeOptions().collectAsState(emptyList())
    val showThemeSetting by homeViewModel.showThemeSetting.collectAsState(false)
    val selectedTheme by homeViewModel.selectedTheme.collectAsState()
    var currentSelectedTheme by remember { mutableStateOf(selectedTheme?.id ?: 1) }
    val isDarkSystem = isSystemInDarkTheme()

    if (showThemeSetting) {
        ModalBottomSheet(
            onDismissRequest = { homeViewModel.changeshowThemeSetting(false) }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Seleccionar Tema",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                }

                items(themeOptions) { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                currentSelectedTheme = theme.id
                                homeViewModel.updateThemePreference(theme, isDarkSystem)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var icon = TablerIcons.Sun
                            if (theme.dark) {
                                icon = TablerIcons.Moon
                            } else if (theme.system) {
                                icon = TablerIcons.DeviceMobile
                            }

                            Icon(
                                imageVector = icon,
                                contentDescription = theme.theme,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = theme.theme,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        RadioButton(
                            selected = currentSelectedTheme == theme.id,
                            onClick = {
                                currentSelectedTheme = theme.id
                                homeViewModel.updateThemePreference(theme, isDarkSystem)
                            }
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}