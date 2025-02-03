package org.example.aok.features.common.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import org.jetbrains.compose.resources.painterResource
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.facebook
import aok.composeapp.generated.resources.instagram
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import aok.composeapp.generated.resources.tiktok
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField
import org.example.aok.ui.components.alerts.MyInfoAlert
import org.example.aok.ui.components.alerts.MySuccessAlert

import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import androidx.compose.runtime.*
import androidx.compose.material3.*
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.aok.core.logInfo
import org.example.aok.ui.components.SocialMedia


@OptIn(ExperimentalMaterial3Api::class)
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
    val error: String? by loginViewModel.error.collectAsState(null)
    val showBottomSheet by homeViewModel.showBottomSheet.collectAsState(false)
    val showResponse by loginViewModel.showResponse.collectAsState()
    val response by loginViewModel.response.collectAsState()



    val imageLogo =
        if (isSystemInDarkTheme()) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 36.dp, end = 36.dp, bottom = 56.dp, top = 112.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            MyCircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(imageLogo),
                    contentDescription = "logo",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { loginViewModel.onLoginChanged(it, password) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Usuario") },
                    label = { Text(text = "Usuario") },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
//                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
//                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { loginViewModel.onLoginChanged(username, it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Contraseña") },
                    label = { Text(text = "Contraseña") },
                    visualTransformation = if(!verPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
//                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
//                    ),
                    trailingIcon = {
                        if (password.isNotBlank()) {
                            PasswordIcon(
                                isPasswordVisible = verPassword,
                                onIconClick = { loginViewModel.togglePasswordVisibility() }
                            )
                        }
                    }
                )

//                MyOutlinedTextField(
//                    value = password,
//                    onValueChange = { loginViewModel.onLoginChanged(username, it) },
//                    placeholder = "Contraseña",
//                    label = "Contraseña",
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                    modifier = Modifier.fillMaxWidth()
//                )

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
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "More Information",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    TextButton(
                        onClick = {
                            loginViewModel.tryToAuth()
                        }
                    ) {
                        Text(
                            text = "Ingresar con huella o Face ID",
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Fingerprint,
                            contentDescription = "More Information",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = {
                        loginViewModel.onLoginSelector(navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = loginViewModel.habilitaBoton(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = "Ingresar",
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SocialMedia(
                    modifier = Modifier,
                    homeViewModel = homeViewModel
                )
            }
        }

        if (error != null) {
            MyErrorAlert(
                titulo = "Error",
                mensaje = error!!,
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
    val image = if (!isPasswordVisible) {
        Icons.Default.Visibility
    } else {
        Icons.Default.VisibilityOff
    }
    val description = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
    IconButton(onClick = onIconClick) {
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
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
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
