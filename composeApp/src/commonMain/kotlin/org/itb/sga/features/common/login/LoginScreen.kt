package org.itb.sga.features.common.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.alerts.MySuccessAlert
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.itb.sga.ui.components.SocialMedia

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel
) {
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val verPassword: Boolean by loginViewModel.verPassword.collectAsState(false)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            MyCircularProgressIndicator()
        } else {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(imageLogo),
                        contentDescription = "logo",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    MyOutlinedTextField(
                        value = username,
                        onValueChange = { loginViewModel.onLoginChanged(it, password) },
                        placeholder = "Usuario",
                        label = "Usuario",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MyOutlinedTextField(
                        value = password,
                        onValueChange = { loginViewModel.onLoginChanged(username, it) },
                        placeholder = "Contraseña",
                        label = "Contraseña",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if(!verPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            if (password.isNotBlank()) {
                                PasswordIcon(
                                    isPasswordVisible = verPassword,
                                    onIconClick = { loginViewModel.togglePasswordVisibility() }
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "More Information",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        TextButton(
                            onClick = {
                                loginViewModel.tryToAuth(navController, homeViewModel.aokDatabase)
                            }
                        ) {
                            Text(
                                text = "Ingresar con huella o Face ID",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.Fingerprint,
                                contentDescription = "More Information",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    MyFilledTonalButton(
                        text = "Ingresar",
                        enabled = loginViewModel.habilitaBoton(),
                        icon = Icons.Filled.Login,
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

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    SocialMedia(
                        modifier = Modifier,
                        homeViewModel = homeViewModel,
                        size = 40.dp
                    )
                }
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
fun PasswordIcon(
    isPasswordVisible: Boolean,
    onIconClick: () -> Unit
) {
    val image = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
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
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
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
                    // Filtra solo los dígitos y limita la longitud a 10
                    val sanitizedInput = newPhoneInput.filter { it.isDigit() }.take(10)
                    loginViewModel.onPasswordRecoveryChange(userInput, sanitizedInput)
                },
                placeholder = "Celular",
                label = "Celular",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "Enviar",
                    enabled = true,
                    icon = Icons.Filled.Save,
                    iconSize = 20.dp,
                    onClickAction = {
                        homeViewModel.changeBottomSheet()
                        loginViewModel.requestPasswordRecovery()
                    }
                )
            }
        }
    }
}
