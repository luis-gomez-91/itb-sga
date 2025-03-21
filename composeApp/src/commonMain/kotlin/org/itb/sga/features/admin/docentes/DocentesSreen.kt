package org.itb.sga.features.admin.docentes

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.Docente
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.Paginado
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data?.profesores ?: emptyList()) { docente ->
                        DocenteItem(
                            docente = docente,
                            loginViewModel = loginViewModel,
                            homeViewModel = homeViewModel,
                            navController = navController
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            data?.paging?.let {
                Paginado(
                    isLoading = isLoading,
                    paging = it,
                    homeViewModel = homeViewModel,
                    onBack = {
                        homeViewModel.pageLess()
                        docentesViewModel.onloadDocentes(
                            query,
                            actualPage - 1,
                            homeViewModel
                        )
                    },
                    onNext = {
                        homeViewModel.pageMore()
                        homeViewModel.pageMore()
                        docentesViewModel.onloadDocentes(
                            query,
                            actualPage + 1,
                            homeViewModel
                        )
                    }
                )
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

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Top
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

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = docente.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(8.dp))
                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse options" else "Expand options",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = formatoText("Identificación:", docente.identificacion),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = formatoText("Usuario:", docente.username),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Text(
                                text = formatoText("Correo institucional:", docente.email),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = formatoText("Correo personal:", docente.email_personal),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            docente.celular?.let {
                                Text(
                                    text = formatoText("Teléfono celular:", it),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            docente.convencional?.let {
                                Text(
                                    text = formatoText("Teléfono convencional:", it),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

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
    HorizontalDivider()
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
                            homeViewModel.clearHomeData()
                            homeViewModel.clearSearchQuery()
                            loginViewModel.changeLogin(docente.idUsuario, navController)
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
                            style = MaterialTheme.typography.labelMedium,
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
                            style = MaterialTheme.typography.labelMedium,
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
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}