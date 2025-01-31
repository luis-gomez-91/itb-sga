package org.example.aok.features.student.alu_solicitud_beca.ficha_socioeconomica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel

@Composable
fun FichaSocioeconomicaScreen(
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel,
    homeViewModel: HomeViewModel,
    scope: CoroutineScope
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Datos personales" to Icons.Filled.Person,
        "Referencia familiar" to Icons.Filled.People,
        "Situación familiar" to Icons.Filled.People,
        "Trabajo" to Icons.Filled.Work,
        "Situación habitacional" to Icons.Filled.Home,
        "Datos económicos" to Icons.Filled.AttachMoney,
        "Ingresos Egresos" to Icons.Filled.TrendingUp,
        "Salud" to Icons.Filled.MedicalServices,
        "Datos académicos" to Icons.Filled.School
    )

    LaunchedEffect(Unit) {
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluSolicitudBecaViewModel.onloadFichaSocioeconomica(
                it
            )
        }
    }

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainer),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, pair ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = pair.first,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first,
                            tint = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> DatosPersonales(aluSolicitudBecaViewModel, scope)
            1 -> ReferenciaFamiliar(aluSolicitudBecaViewModel)
//            1 -> Familia(aluSolicitudBecaViewModel)
            2 -> Text("Contenido de Trabajo", modifier = Modifier.padding(16.dp))
            3 -> Text("Contenido de Situación Habitacional", modifier = Modifier.padding(16.dp))
            4 -> Text("Contenido de Datos Económicos", modifier = Modifier.padding(16.dp))
            5 -> Text("Contenido de Ingresos Egresos", modifier = Modifier.padding(16.dp))
            6 -> Text("Contenido de Salud", modifier = Modifier.padding(16.dp))
            7 -> Text("Contenido de Datos Académicos", modifier = Modifier.padding(16.dp))
        }
    }
}