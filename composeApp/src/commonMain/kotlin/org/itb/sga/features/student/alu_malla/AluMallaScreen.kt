package org.itb.sga.features.student.alu_malla

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.AluMalla
import org.itb.sga.data.network.AluMallaAsignatura
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluMallaScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluMallaViewModel: AluMallaViewModel
) {
    DashBoardScreen(
        title = "Mi Malla",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluMallaViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@UiComposable
fun Screen(
    homeViewModel: HomeViewModel,
    aluMallaViewModel: AluMallaViewModel
) {
    val data by aluMallaViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val pagerState = rememberPagerState { data.size }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluMallaViewModel.onloadAluMalla(
                it, homeViewModel
            )
        }
    }

    LaunchedEffect(data) {
        selectedTabIndex = 0
        pagerState.scrollToPage(0)
    }

    val mallaFiltrada = if (searchQuery.isNotEmpty()) {
        data.filter { item ->
            item.asignaturas.any { asignatura ->
                asignatura.asignaturaNombre.contains(searchQuery, ignoreCase = true)
            }
        }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if (mallaFiltrada.isNotEmpty()) {
                LaunchedEffect(selectedTabIndex) {
                    pagerState.animateScrollToPage(selectedTabIndex)
                }
                LaunchedEffect(pagerState.currentPage) {
                    selectedTabIndex = pagerState.currentPage
                }
                ScrollableTabRowNivelesMalla(
                    data = data,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index
                    }
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                ) { index ->
                    Asignaturas(data[selectedTabIndex])
                }
            } else {
                Text(
                    text = "No hay datos disponibles",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ScrollableTabRowNivelesMalla(
    data: List<AluMalla>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {

    val nivelIconos = listOf(
        Icons.Filled.MenuBook,
        Icons.Filled.ShowChart,
        Icons.Filled.School,
        Icons.Filled.Edit,
        Icons.Filled.Science,
        Icons.Filled.Work,
        Icons.Filled.EmojiEvents,
        Icons.Filled.Stairs,
        Icons.Filled.MenuBook,
        Icons.Filled.ShowChart,
        Icons.Filled.School,
        Icons.Filled.Edit,
        Icons.Filled.Science,
        Icons.Filled.Work,
        Icons.Filled.EmojiEvents,
        Icons.Filled.Stairs,
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        data.forEachIndexed { index, aluMalla ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = aluMalla.nivelmallaNombreCorto ?: "N/A",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = nivelIconos.getOrElse(index) { Icons.Filled.MenuBook },
                        contentDescription = null,
                        tint = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    )
                }
            )
        }
    }
}

@Composable
fun Asignaturas(
    nivelMalla: AluMalla?
) {
    nivelMalla?.let { malla ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(malla.asignaturas) { asignatura ->
                AsignaturaItem(asignatura)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } ?: run {
        Text(text = "No hay asignaturas disponibles", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AsignaturaItem(
    asignatura: AluMallaAsignatura
) {
    val cardStyle = when {
        asignatura.aprobado == true -> CardStyle(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            color = MaterialTheme.colorScheme.onPrimary,
            icono = Icons.Filled.CheckCircle,
            estado = "APROBADO"
        )
        asignatura.reprobado == true -> CardStyle(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            color = MaterialTheme.colorScheme.error,
            icono = Icons.Filled.Error,
            estado = "REPROBADO"
        )
        else -> CardStyle(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            color = MaterialTheme.colorScheme.onSurface,
            icono = Icons.Filled.Pending,
            estado = "PENDIENTE"
        )
    }

    MyCard(
        Modifier,
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = asignatura.asignaturaNombre,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = formatoText("Identificación:", asignatura.asignaturaMallaIdent),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatoText("Eje formativo:", asignatura.ejeFormativo),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyAssistChip(
                    label = cardStyle.estado,
                    containerColor = cardStyle.containerColor,
                    labelColor = cardStyle.color,
                    icon = cardStyle.icono
                )
            }
            Spacer(Modifier.height(4.dp))
            if (asignatura.record) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatoText("Nota:", asignatura.nota),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatoText("Asistencia:", "${asignatura.asistencia}%"),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        }
    }

}



data class CardStyle(
    val containerColor: Color,
    val color: Color,
    val icono: ImageVector,
    val estado: String
)