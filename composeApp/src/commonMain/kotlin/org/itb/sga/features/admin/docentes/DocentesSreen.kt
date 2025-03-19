package org.itb.sga.features.admin.docentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.itb.sga.data.network.Docente
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun DocentesScreen(
    navController: NavHostController,
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
    val actualPage by homeViewModel.actualPage.collectAsState(1)

    homeViewModel.changeFastSearch(false)

    LaunchedEffect(query) {
        docentesViewModel.onloadDocentes(query, actualPage, homeViewModel)
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
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        homeViewModel.pageLess()
                        docentesViewModel.onloadDocentes(
                            query,
                            actualPage,
                            homeViewModel
                        )
                    },
                    enabled = actualPage > 1  && !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "Back",
                        tint = if (actualPage > 1  && !isLoading) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
                    )
                }

                Text(
                    text = "${actualPage}/${data?.paging?.lastPage?: 0}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )

                IconButton(
                    onClick = {
                        homeViewModel.pageMore()
                        docentesViewModel.onloadDocentes(
                            query,
                            actualPage,
                            homeViewModel
                        )
                    },
                    enabled = actualPage < (data?.paging?.lastPage ?: Int.MAX_VALUE) && !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Next",
                        tint = if (actualPage < (data?.paging?.lastPage ?: Int.MAX_VALUE)  && !isLoading) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
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
    var showActions by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model = docente.foto,
                contentDescription = "Foto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(64.dp)
                    .aspectRatio(1 / 1f)
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

                        Column {
                            IconButton(
                                onClick = {
                                    showActions = !showActions
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "More Information",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Box {
                                if (showActions) {
                                    DropdownDocente(
                                        loginViewModel = loginViewModel,
                                        homeViewModel = homeViewModel,
                                        navController = navController,
                                        docente = docente,
                                        onDismissRequest = { showActions = false }
                                    )
                                }
                            }
                        }
                    }
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
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
    }
    DetalleDocente(docente, expanded)
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
            modifier = Modifier.padding(horizontal = 16.dp),
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

@Composable
fun DropdownDocente(
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    docente: Docente,
    onDismissRequest: () -> Unit
) {
    Popup (
        alignment = Alignment.TopStart,
        properties = PopupProperties(),
        onDismissRequest = onDismissRequest
    ){
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            loginViewModel.changeLogin(docente.idUsuario, navController)
                            homeViewModel.clearSearchQuery()
                            navController.navigate("home")
                        }
                    ){
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.Login,
                            contentDescription = "Login",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Login",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.School,
                            contentDescription = "Clases",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Clases",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.Notes,
                            contentDescription = "Calificaciones",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Calificaciones",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}