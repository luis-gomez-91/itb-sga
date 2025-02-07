package org.example.aok.features.student.alu_consulta_general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.logInfo
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralAlumno
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralCronograma
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralFinanza
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralMatricula
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen
import org.example.aok.ui.components.text.MyMediumParagraphFormat
import org.example.aok.ui.components.text.MyMediumTitle
import org.example.aok.ui.components.text.MySmallParagraphFormat
import org.example.aok.ui.components.text.MySmallTitle

@Composable
fun AluConsultaGeneralScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluConsultaGeneralViewModel: AluConsultaGeneralViewModel
) {
    DashBoardScreen(
        title = "Consulta General",
        navController = navController,
        content = {
            Screen(
                navController,
                homeViewModel,
                aluConsultaGeneralViewModel
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
    aluConsultaGeneralViewModel: AluConsultaGeneralViewModel
) {
    val data by aluConsultaGeneralViewModel.data.collectAsState(null)
    val error by homeViewModel.error.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    logInfo("prueba", "data: ${homeViewModel.homeData.value}")

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluConsultaGeneralViewModel.onloadAluConsultaGeneral(
                it, homeViewModel)
        }
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal =  16.dp)
        ) {
            item { data?.let { dataAlumno(it.alumno) } }
            item { data?.let { it.finanzas?.let { finanzas -> dataFinanzas(finanzas) } } }
            item { data?.let { it.matricula?.let { matricula -> dataMatricula(matricula) } } }
            item { data?.let { it.cronograma?.let { cronograma -> dataCronograma(cronograma) } } }
        }
    }
}

@Composable
fun dataCronograma(
    data: List<ConsultaGeneralCronograma>
) {
    MyCard {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            MyMediumTitle("Cronograma")
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(data) { materia ->
                    MyCard (
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            MySmallTitle(materia.materia)
                            Spacer(Modifier.height(8.dp))
                            MySmallParagraphFormat("Fecha: ", "${materia.fechaInicio} al ${materia.fechaFin}")
                            Spacer(Modifier.height(4.dp))
                            MySmallParagraphFormat("Docente: ", materia.docente?.let { it } ?: "Sin asignar")
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataFinanzas(
    data: List<ConsultaGeneralFinanza>
) {
    MyCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            MyMediumTitle("Finanzas")
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(data) { rubro ->
                    MyCard (
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            MySmallTitle(rubro.nombre)
                            Spacer(Modifier.height(8.dp))
                            MySmallParagraphFormat("Fecha Vencimiento: ", "${rubro.fechaVence}")
                            Spacer(Modifier.height(4.dp))
                            MySmallParagraphFormat("Valor: ", "$${rubro.valor}")
                            Spacer(Modifier.height(4.dp))
                            MySmallParagraphFormat("Abono: ", "$${rubro.abono}")
                            Spacer(Modifier.height(4.dp))
                            MySmallParagraphFormat("Saldo: ", "$${rubro.saldo}")
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataAlumno(
    data: ConsultaGeneralAlumno
) {
    MyCard {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            MyMediumTitle("Datos del alumno")

            Spacer(Modifier.height(8.dp))
            MyMediumParagraphFormat("Nombre: ", data.nombre)

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Celular: ", "${data.celular}")

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Correo: ", data.email)

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Tiene discapacidad: ", if (data.dicapacitado) "Si" else "No")

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Carrera: ", data.carrera)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataMatricula(
    data: ConsultaGeneralMatricula
) {
    MyCard {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            MyMediumTitle("Matrícula")

            Spacer(Modifier.height(8.dp))
            MyMediumParagraphFormat("Grupo: ", data.grupo)

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Nivel: ", data.nivel)

            Spacer(Modifier.height(4.dp))
            MyMediumParagraphFormat("Jornada: ", "${data.sesion} (${data.horaInicio} a ${data.horaFin})")
        }
    }
    Spacer(Modifier.height(8.dp))
}