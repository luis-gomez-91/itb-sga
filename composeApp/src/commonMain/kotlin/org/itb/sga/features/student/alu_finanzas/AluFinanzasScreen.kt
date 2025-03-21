package org.itb.sga.features.student.alu_finanzas

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.itb.sga.core.formatoText
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Rubro
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluFinannzasScreen(
    navController: NavHostController,
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
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluFinanzasViewModel.onloadAluFinanzas(
                it, homeViewModel)
        }
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
    val vencimientoDate = LocalDate.parse(rubro.fechaVencimiento)
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    logInfo("posi", "${vencimientoDate}")
    logInfo("posi", "${currentDate}")

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
        vencimientoDate < currentDate -> {
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
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Row() {
                MyAssistChip(
                    label = formatoText("Fecha vencimiento:", rubro.fechaVencimiento).toString(),
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
                    text = formatoText("Valor:", "$${rubro.valor}"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = formatoText("Abono:", "$${rubro.pagado}"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = formatoText("Saldo:", "$${rubro.porPagar}"),
                    style = MaterialTheme.typography.labelMedium,
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
