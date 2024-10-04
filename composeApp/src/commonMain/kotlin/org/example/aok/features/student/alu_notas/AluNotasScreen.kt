package org.example.aok.features.student.alu_notas

import CustomLinearProgressIndicator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CardDefaults


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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.data.network.AluNotas
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard

import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluNotasScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluNotasViewModel: AluNotasViewModel
) {
    DashBoardScreen(
        title = "Registro Académico",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluNotasViewModel
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
    aluNotasViewModel: AluNotasViewModel
) {
    val data: List<AluNotas> by aluNotasViewModel.data.collectAsState(emptyList())
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

    val dataFiltrada = if (searchQuery.isNotEmpty()) {
        data.filter { it.asignatura.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        aluNotasViewModel.onloadAluCronograma(homeViewModel.homeData.value!!.persona.idInscripcion!!)
    }

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        items(dataFiltrada) { data ->
            AluCronogramaItem(data)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AluCronogramaItem(
    data: AluNotas
) {
    // Estado para expandir o colapsar
    var expanded by remember { mutableStateOf(false) }

    // Estado animado para el progreso de asistencia
    val animatedProgress by animateFloatAsState(
        targetValue = if (expanded) (data.asistencia / 100f).toFloat() else 0f,  // Animar solo si está expandido
        animationSpec = tween(durationMillis = 1000)  // Duración de la animación
    )

    // Definir colores según el estado
    val estadoColor = if (data.estado.equals("APROBADA", ignoreCase = true)) {
        MaterialTheme.colorScheme.onPrimary // Verde para "APROBADA"
    } else {
        MaterialTheme.colorScheme.secondaryContainer // Otro color para los demás estados
    }

    // Tarjeta interactiva que alterna expandido/colapsado
    MyCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { expanded = !expanded },  // Cambiar estado al hacer clic
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Fila superior con asignatura, nota y estado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = data.asignatura,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                MyAssistChip(
                    label = "${data.nota} nota",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Chip de estado
                MyAssistChip(
                    label = data.estado,
                    containerColor = estadoColor.copy(alpha = 0.1f),
                    labelColor = estadoColor
                )
            }

            // Mostrar más información solo si está expandido
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso para la asistencia
                Text(
                    text = "Asistencia: ${data.asistencia}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Barra de progreso animada
                CustomLinearProgressIndicator(
                    progress = animatedProgress,  // Progreso animado
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,  // Color del indicador de progreso
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,  // Color de fondo
                    height = 8.dp,  // Altura de la barra de progreso
                    cornerRadius = 4.dp  // Radio de las esquinas
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila con fecha y convalidación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyAssistChip(
                        label = "${data.fecha} fecha",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary,
                        icon = Icons.Filled.DateRange
                    )
                    MyAssistChip(
                        label = if(data.convalidacion) "Sí convalidacion" else "No convalidacion",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

