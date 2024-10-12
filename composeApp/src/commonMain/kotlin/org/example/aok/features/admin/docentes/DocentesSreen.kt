package org.example.aok.features.admin.docentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.example.aok.core.MainViewModel
import org.example.aok.data.network.Docente
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun DocentesScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    docentesViewModel: DocentesViewModel
) {
    DashBoardScreen(
        title = "Docentes",
        navController = navController,
        content = {
            Screen(
                navController,
                homeViewModel,
                docentesViewModel,
                loginViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    docentesViewModel: DocentesViewModel,
    loginViewModel: LoginViewModel

) {
    val data by docentesViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")

    if (query.isNotBlank()) {
        docentesViewModel.onloadDocentes(query)
    }
    homeViewModel.changeFastSearch(false)
    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.changeFastSearch(false)
        docentesViewModel.onloadDocentes("")
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(data?.profesores ?: emptyList()) { docente ->
                        DocenteItem(
                            docente = docente,
                            loginViewModel = loginViewModel,
                            homeViewModel = homeViewModel,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                IconButton(
                    onClick = {
                        docentesViewModel.onloadDocentes(
                            search = ""
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        docentesViewModel.onloadDocentes(
                            search = ""
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

    }
}

@Composable
fun DocenteItem(
    docente: Docente,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.Top,
    ){
        AsyncImage(
            model = docente.foto,
            contentDescription = "Foto",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(64.dp)
                .aspectRatio(1/1f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = docente.nombre,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )

                Row {
                    MyAssistChip(
                        label = "Cédula: ${docente.identificacion}",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MyAssistChip(
                        label = docente.username,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            loginViewModel.changeLogin(docente.idPersona, docente.nombre)
                            homeViewModel.clearSearchQuery()
                            navController.navigate("home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Login,
                            contentDescription = "Login",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                DetalleDocente(docente, expanded)
            }

            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }


    }
    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)
}

@Composable
fun DetalleDocente(
    docente: Docente,
    expanded: Boolean
) {
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        MyCard (
            modifier = Modifier.padding(bottom = 4.dp),
            onClick = { }
        ) {
            Column {
                Text(
                    text = "Correo institucional: ${docente.email}",
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Correo personal: ${docente.email_personal}",
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}