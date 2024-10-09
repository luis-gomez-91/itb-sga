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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import org.example.aok.ui.components.MyAlert
import org.example.aok.ui.components.MyCircularProgressIndicator


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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    trailingIcon = {
                        if (password.isNotBlank()) {
                            PasswordIcon(
                                isPasswordVisible = verPassword,
                                onIconClick = { loginViewModel.togglePasswordVisibility() }
                            )
                        }
                        else {
                            null
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Recuperar contraseña",
                        modifier = Modifier
                            .clickable {
//                            navController.navigate("forgot_password")
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
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
                RedesSociales(
                    modifier = Modifier,
                    homeViewModel = homeViewModel
                )
            }
        }

        if (error != null) {
            MyAlert(
                titulo = "Error",
                mensaje = error!!,
                onDismiss = {
                    loginViewModel.clearError()
                },
                showAlert = true
            )
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

    IconButton(onClick = onIconClick) {
        androidx.compose.material3.Icon(imageVector = image, contentDescription = null)
    }
}

@Composable
fun RedesSociales(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            homeViewModel.openURL(url = "https://www.instagram.com/itb_ec")
        }) {
            Image(
                painter = painterResource(Res.drawable.instagram),
                contentDescription = "Instagram",
                modifier = Modifier.size(120.dp)
            )
        }
        IconButton(onClick = {
            homeViewModel.openURL(url = "https://www.facebook.com/itb.edu.ec")
        }) {
            Image(
                painter = painterResource(Res.drawable.facebook),
                contentDescription = "Facebook",
                modifier = Modifier.size(120.dp)
            )
        }
        IconButton(onClick = {
            homeViewModel.openURL(url = "https://www.tiktok.com/@itb_ec?lang=es")
        }) {
            Image(
                painter = painterResource(Res.drawable.tiktok),
                contentDescription = "Tik Tok",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}