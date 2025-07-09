package org.itb.sga.features.common.docBiblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.SERVER_URL
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.Paginado
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).weight(1f)
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

            Spacer(Modifier.height(4.dp))

            data?.paging?.let {
                Paginado(
                    isLoading = isLoading,
                    paging = it,
                    homeViewModel = homeViewModel,
                    onBack = {
                        homeViewModel.pageLess()
                        docBibliotecaViewModel.onloadDocBiblioteca(
                            query,
                            actualPage - 1,
                            homeViewModel
                        )
                    },
                    onNext = {
                        homeViewModel.pageMore()
                        docBibliotecaViewModel.onloadDocBiblioteca(
                            query,
                            actualPage + 1,
                            homeViewModel
                        )
                    }
                )
            }
        }
    }
    error?.let {
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                homeViewModel.clearError()
                navController.popBackStack()
            },
            showAlert = true
        )
    }
}