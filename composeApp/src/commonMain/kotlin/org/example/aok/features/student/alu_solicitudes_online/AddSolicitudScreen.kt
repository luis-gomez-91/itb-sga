package org.example.aok.features.student.alu_solicitudes_online

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.example.aok.features.common.home.HomeViewModel

@Composable
fun AddSolicitudScreen(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    homeViewModel: HomeViewModel
) {

    LaunchedEffect(Unit) {
        aluSolicitudesViewModel.onloadAddForm(homeViewModel)
    }

    Button(
        onClick = { aluSolicitudesViewModel.changeShowForm() }
    ) {
        Text("REGRESAR")
    }
}