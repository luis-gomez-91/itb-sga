package org.example.aok.features.student.alu_finanzas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.aok.core.MainViewModel
import org.example.aok.core.formatoText
import org.example.aok.core.logInfo
import org.example.aok.data.network.Rubro
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluFinannzasScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluFinanzasViewModel: AluFinanzasViewModel
) {
    DashBoardScreen(
        title = "Mis Finanzas",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluFinanzasViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluFinanzasViewModel: AluFinanzasViewModel
) {
    val data by aluFinanzasViewModel.data.collectAsState(emptyList())
    val isLoading by aluFinanzasViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        aluFinanzasViewModel.onloadAluFinanzas(homeViewModel.homeData.value!!.persona.idPersona)
    }

    val rubrosFiltados = if (searchQuery.isNotEmpty()) {
        data.filter { it.rubro.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(rubrosFiltados) { rubro ->
                    CardAluFinanza(
                        rubro = rubro
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CardAluFinanza(
    rubro: Rubro
) {
    val date = LocalDate.parse(rubro.fechaVencimiento)
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val cardStyle = when {
        rubro.cancelado -> {
            logInfo("home", "Cancelado: ${rubro.cancelado}, Fecha vencimiento: ${rubro.fechaVencimiento}")
            CardStyle(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                color = MaterialTheme.colorScheme.onPrimary,
                icono = Icons.Filled.MonetizationOn,
                estado = "Pagado"
            )
        }
        date > currentDate -> {
            logInfo("home", "Fecha vencida: ${rubro.fechaVencimiento}, Actual: $currentDate")
            CardStyle(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                color = MaterialTheme.colorScheme.error,
                icono = Icons.Filled.Warning,
                estado = "Vencido"
            )
        }
        else -> {
            logInfo("home", "Pendiente: ${rubro.fechaVencimiento}")
            CardStyle(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                icono = Icons.Filled.AttachMoney,
                estado = "Pendiente"
            )
        }
    }


    MyCard (
        modifier = Modifier.padding(bottom = 4.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = rubro.rubro,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row() {
                MyAssistChip(
                    label = formatoText("Fecha vencimiento: ", "${rubro.fechaVencimiento}").toString(),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Filled.DateRange
                )

                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = cardStyle.estado,
                    containerColor = cardStyle.containerColor,
                    labelColor = cardStyle.color,
                    icon = cardStyle.icono
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatoText("Valor: ", "$${rubro.valor}"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = formatoText("Abodo: ", "$${rubro.pagado}"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = formatoText("Saldo: ", "$${rubro.porPagar}"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
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
