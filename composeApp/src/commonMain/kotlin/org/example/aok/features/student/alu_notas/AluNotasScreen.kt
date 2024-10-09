package org.example.aok.features.student.alu_notas

import CustomLinearProgressIndicator
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon


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
import org.example.aok.core.capitalizeWords
import org.example.aok.data.network.AluNotas
import org.example.aok.data.network.OtraNotaAsignatura
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.student.alu_malla.CardStyle
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
    val data by aluNotasViewModel.data.collectAsState(null)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

    val asignaturas = data?.asignaturas
    val otraNotas = data?.otrasnotas

    val dataFiltrada = if (searchQuery.isNotEmpty()) {
        asignaturas?.filter { it.asignatura.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        aluNotasViewModel.onloadAluNotaAsignatura(homeViewModel.homeData.value!!.persona.idInscripcion!!)
    }

    TipoItem(asignaturas, otraNotas)
}


@Composable
fun TipoItem(
    asignaturas: List<AluNotas>?,
    otraNotaAsignatura: List<OtraNotaAsignatura>?
) {
    var expanded by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)  // Hacer la columna desplazable
            .padding(horizontal = 16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { expanded = !expanded }
        ) {
            AnimatedContent(targetState = expanded) { isExpanded ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Asignaturas".capitalizeWords(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // Mostrar las asignaturas solo si la tarjeta está expandida
        if (expanded) {
            asignaturas?.forEach { asignatura ->
                Spacer(modifier = Modifier.height(8.dp))
                AluNotaItem(data = asignatura)  // Aquí muestras cada AluNotaItem
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        // -------------------------------------------------------------- //

        var expanded2 by remember { mutableStateOf(true) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { expanded2 = !expanded2 }
        ) {
            AnimatedContent(targetState = expanded2) { isExpanded ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Otras Notas".capitalizeWords(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (expanded2) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // Mostrar otras notas solo si la tarjeta está expandida
        if (expanded2) {
            otraNotaAsignatura?.forEach { asignatura ->
                Spacer(modifier = Modifier.height(8.dp))
                OtraNotaItem(data = asignatura)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun AluNotaItem(
    data: AluNotas?
) {
    data?.let { nota ->
        // Estado para expandir o colapsar
        var expanded by remember { mutableStateOf(false) }

        // Estado animado para el progreso de asistencia
        val animatedProgress by animateFloatAsState(
            targetValue = if (expanded) (data.asistencia / 100f).toFloat() else 0f,  // Animar solo si está expandido
            animationSpec = tween(durationMillis = 1000)  // Duración de la animación
        )

        val estadoColor = when {
            data.estado == "APROBADA" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                color = MaterialTheme.colorScheme.onPrimary,
                icono = Icons.Filled.CheckCircle,
                estado = "APROBADA"
            )
            data.estado == "REPROBADA" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                color = MaterialTheme.colorScheme.error,
                icono = Icons.Filled.Close,
                estado = "REPROBADA"
            )
            else -> CardStyle(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                color = MaterialTheme.colorScheme.onSurface,
                icono = Icons.Filled.Pending,
                estado = "PENDIENTE"
            )
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
                        containerColor = estadoColor.containerColor,
                        labelColor = estadoColor.color
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
}

@Composable
fun OtraNotaItem(
    data: OtraNotaAsignatura?
) {
    data?.let { nota ->
        // Estado para expandir o colapsar
        var expanded by remember { mutableStateOf(false) }

        val estadoColor = when {
            data.estado == "APROBADO" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                color = MaterialTheme.colorScheme.onPrimary,
                icono = Icons.Filled.CheckCircle,
                estado = "APROBADO"
            )
            data.estado == "REPROBADO" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                color = MaterialTheme.colorScheme.error,
                icono = Icons.Filled.Close,
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
                        text = data.nombre,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    MyAssistChip(
                        label = "${data.nota1} nota1",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Chip de estado
                    MyAssistChip(
                        label = data.estado,
                        containerColor = estadoColor.containerColor,
                        labelColor = estadoColor.color
                    )
                }

                // Mostrar más información solo si está expandido
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    // Fila con fecha y convalidación
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyAssistChip(
                            label = "${data.nota2} nota2",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary
                        )
                        MyAssistChip(
                            label = "${data.nota3} nota3",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary
                        )
                        MyAssistChip(
                            label = "${data.nota4} nota4",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

