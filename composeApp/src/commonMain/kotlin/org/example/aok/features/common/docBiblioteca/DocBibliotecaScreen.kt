package org.example.aok.features.common.docBiblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.Paging
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen
import kotlin.random.Random

@Composable
fun DocBibliotecaScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    docBibliotecaViewModel: DocBibliotecaViewModel
) {
    DashBoardScreen(
        title = "Biblioteca",
        navController = navController,
        content = {
            Screen(
                navController,
                homeViewModel,
                docBibliotecaViewModel
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
    docBibliotecaViewModel: DocBibliotecaViewModel
) {
    val data by docBibliotecaViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val actualPage by homeViewModel.actualPage.collectAsState(2)
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(query) {
        docBibliotecaViewModel.onloadDocBiblioteca(query, actualPage, homeViewModel)
    }

    val dataFiltrada = data?.documentos?.let { documentos ->
        if (query.isNotEmpty()) {
            documentos.filter { it.nombre.contains(query, ignoreCase = true) }
        } else {
            documentos
        }
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing  = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1f)
            ) {
                dataFiltrada?.let {
                    items(it) { documento ->
                        val randomColor = Color(
                            red = Random.nextFloat(),
                            green = Random.nextFloat(),
                            blue = Random.nextFloat(),
                            alpha = 0.15f
                        )

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = randomColor)
                                .clickable(onClick = {
                                    homeViewModel.openURL("${SERVER_URL}media/${documento.url}")
                                })
                        ) {
                            val width = maxWidth
                            val itemHeight = width * 4f / 3f
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .height(itemHeight),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = documento.nombre,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    textAlign = TextAlign.Center
                                )
                                Column (
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    documento.autor?.let { author ->
                                        Text(
                                            text = author,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.End,
                                        )
                                    }
                                    Text(
                                        text = documento.anno.toString(),
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            data?.let {
                Paginado(
                    homeViewModel,
                    docBibliotecaViewModel,
                    query,
                    isLoading,
                    it.paging
                )
            }
        }

        if (error != null) {
            MyErrorAlert(
                titulo = error!!.title,
                mensaje = error!!.error,
                onDismiss = {
                    homeViewModel.clearError()
                    navController.popBackStack()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun Paginado(
    homeViewModel: HomeViewModel,
    docBibliotecaViewModel: DocBibliotecaViewModel,
    query: String,
    isLoading: Boolean,
    paging: Paging
) {
    val actualPage by homeViewModel.actualPage.collectAsState(1)

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
                docBibliotecaViewModel.onloadDocBiblioteca(
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
            text = "${actualPage}/${paging.lastPage}",
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )

        IconButton(
            onClick = {
                homeViewModel.pageMore()
                docBibliotecaViewModel.onloadDocBiblioteca(
                    query,
                    actualPage,
                    homeViewModel
                )
            },
            enabled = actualPage < (paging.lastPage) && !isLoading
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = if (actualPage < (paging.lastPage)  && !isLoading) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}