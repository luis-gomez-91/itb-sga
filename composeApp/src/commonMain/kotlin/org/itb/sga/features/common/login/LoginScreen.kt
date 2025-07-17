package org.itb.sga.features.common.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.alerts.MySuccessAlert
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import compose.icons.TablerIcons
import compose.icons.tablericons.Eye
import compose.icons.tablericons.EyeOff
import compose.icons.tablericons.FaceId
import compose.icons.tablericons.Fingerprint
import compose.icons.tablericons.LockOpen
import compose.icons.tablericons.Login
import compose.icons.tablericons.Send
import org.itb.sga.core.appIsLastVersion
import org.itb.sga.core.getPlatform
import org.itb.sga.data.domain.BiometryType
import org.itb.sga.ui.components.ConnectivityWrapper
import org.itb.sga.ui.components.FullScreenLoading
import org.itb.sga.ui.components.NewVersionScreen
import org.itb.sga.ui.components.SocialMedia


@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel
) {
    val internetConnected by homeViewModel.konnectivity.isConnectedState.collectAsState()
    val appLastVersion by loginViewModel.appLastVersion.collectAsState(null)


    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)
    var imageLogo by remember { mutableStateOf(Res.drawable.logo) }

    LaunchedEffect(selectedTheme) {
        imageLogo = if (selectedTheme?.dark == true) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }
    }

    ConnectivityWrapper(
        internetConnected,
        homeViewModel
    ) {
        LaunchedEffect(Unit) {
            loginViewModel.fetchLastVersionApp()
        }

        appLastVersion?.let {
            if (appIsLastVersion(it)) {
                Screen(loginViewModel, navController, homeViewModel)
            } else {
                NewVersionScreen(imageLogo)
            }
        }
    }
}

@Composable
fun Screen(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val isLoading by loginViewModel.isLoading.collectAsState()
    val error by loginViewModel.error.collectAsState(null)
    val showBottomSheet by homeViewModel.showBottomSheet.collectAsState(false)
    val showResponse by loginViewModel.showResponse.collectAsState()
    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)
    val response by loginViewModel.response.collectAsState()
    var imageLogo by remember { mutableStateOf(Res.drawable.logo) }

    LaunchedEffect(selectedTheme) {
        imageLogo = if (selectedTheme?.dark == true) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }
    }

    Box {
        FullScreenLoading(isLoading = (isLoading))
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .safeContentPadding()
                    .imePadding()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(imageLogo),
                    contentDescription = "logo",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal =  32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        FormLogin(loginViewModel, navController, homeViewModel)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
            ) {
                SocialMedia(
                    modifier = Modifier,
                    homeViewModel = homeViewModel,
                    size = 32.dp
                )
            }
        }

        error?.let {
            MyErrorAlert(
                titulo = it.title,
                mensaje = it.error,
                onDismiss = { loginViewModel.clearError() },
                showAlert = true
            )
        }

        if (showBottomSheet) {
            PasswordRecoveryForm(homeViewModel, loginViewModel)
        }

        if (showResponse) {
            if (response?.status == "success") {
                response?.message?.let {
                    MySuccessAlert(
                        titulo = "¡Operación realizada con éxito!",
                        mensaje = it,
                        onDismiss = {
                            loginViewModel.showResponseChange(false)
                        },
                        showAlert = true
                    )
                }
            } else {
                response?.message?.let {
                    MyErrorAlert(
                        titulo = "¡Error!",
                        mensaje = it,
                        onDismiss = {
                            loginViewModel.showResponseChange(false)
                        },
                        showAlert = true
                    )
                }
            }
        }
    }
}

@Composable
fun FormLogin(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val verPassword: Boolean by loginViewModel.verPassword.collectAsState(false)
    val focusManager = LocalFocusManager.current
    val biometryType = when(getPlatform().name) {
        "Android" -> BiometryType("Ingresar con huella dactilar", TablerIcons.Fingerprint)
        "iOS" -> BiometryType("Ingresar con Face ID", TablerIcons.FaceId)
        else -> BiometryType("Ingresar con credenciales biométricas", TablerIcons.Fingerprint)
    }

    MyOutlinedTextField(
        value = username,
        onValueChange = { loginViewModel.onLoginChanged(it, password) },
        placeholder = "Usuario",
        label = "Usuario",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    MyOutlinedTextField(
        value = password,
        onValueChange = { loginViewModel.onLoginChanged(username, it) },
        placeholder = "Contraseña",
        label = "Contraseña",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if(!verPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (password.isNotBlank()) {
                PasswordIcon(
                    isPasswordVisible = verPassword,
                    onIconClick = { loginViewModel.togglePasswordVisibility() }
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                loginViewModel.onLoginSelector(navController)
            }
        ),
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        TextButton(
            onClick = {
                homeViewModel.changeBottomSheet()
            }
        ) {
            Text(
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = TablerIcons.LockOpen,
                contentDescription = "Recuperar contraseña",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        TextButton(
            onClick = {
                loginViewModel.tryToAuth(navController, homeViewModel.aokDatabase)
            }
        ) {
            Text(
                text = biometryType.message,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = biometryType.icon,
                contentDescription = "Login biométrico",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    MyFilledTonalButton(
        text = "Ingresar",
        enabled = loginViewModel.habilitaBoton(),
        icon = TablerIcons.Login,
        iconSize = 24.dp,
        buttonColor = MaterialTheme.colorScheme.primaryContainer,
        textColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        textStyle = MaterialTheme.typography.titleMedium,
        onClickAction = {
            loginViewModel.onLoginSelector(navController)
        }
    )

}

@Composable
fun PasswordIcon(
    isPasswordVisible: Boolean,
    onIconClick: () -> Unit
) {
    val image = if (isPasswordVisible) TablerIcons.EyeOff else TablerIcons.Eye
    val description = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"

    IconButton(onClick = { onIconClick() }) {
        Icon(imageVector = image, contentDescription = description)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryForm(
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val userInput by loginViewModel.userInput.collectAsState()
    val phoneInput by loginViewModel.phoneInput.collectAsState()


    ModalBottomSheet(
        onDismissRequest = {
            homeViewModel.changeBottomSheet()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyOutlinedTextField(
                value = userInput,
                onValueChange = { loginViewModel.onPasswordRecoveryChange(it, phoneInput) },
                placeholder = "Usuario",
                label = "Usuario",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            MyOutlinedTextField(
                value = phoneInput,
                onValueChange = { newPhoneInput ->
                    val sanitizedInput = newPhoneInput.filter { it.isDigit() }.take(10)
                    loginViewModel.onPasswordRecoveryChange(userInput, sanitizedInput)
                },
                placeholder = "Celular",
                label = "Celular",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            MyFilledTonalButton(
                text = "Enviar",
                enabled = true,
                icon = TablerIcons.Send,
                iconSize = 24.dp,
                onClickAction = {
                    homeViewModel.changeBottomSheet()
                    loginViewModel.requestPasswordRecovery()
                },
                textStyle = MaterialTheme.typography.titleMedium
            )
        }
    }
}
