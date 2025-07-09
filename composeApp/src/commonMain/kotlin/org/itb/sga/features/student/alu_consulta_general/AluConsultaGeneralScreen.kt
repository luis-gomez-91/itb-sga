package org.itb.sga.features.student.alu_consulta_general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.data.network.alu_consulta_general.ConsultaGeneralAlumno
import org.itb.sga.data.network.alu_consulta_general.ConsultaGeneralCronograma
import org.itb.sga.data.network.alu_consulta_general.ConsultaGeneralFinanza
import org.itb.sga.data.network.alu_consulta_general.ConsultaGeneralMatricula
import org.itb.sga.data.network.alu_consulta_general.ConsultaGeneralModulo
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen
import org.itb.sga.ui.components.text.MyMediumParagraphFormat
import org.itb.sga.ui.components.text.MyMediumTitle
import org.itb.sga.ui.components.text.MySmallParagraph
import org.itb.sga.ui.components.text.MySmallParagraphFormat
import org.itb.sga.ui.components.text.MySmallTitle

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

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value?.persona?.idInscripcion?.let {
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
            item { data?.let { dataAlumno(it.alumno, navController) } }
            item { data?.let { dataFinanzas(it.finanzas, navController) } }
            item { data?.let { it.matricula?.let { matricula -> dataMatricula(matricula, navController) } } }
            item { data?.let { it.cronogramas?.let { cronograma -> dataCronograma(cronograma, navController) } } }
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

@Composable
fun dataCronograma(
    data: ConsultaGeneralCronograma,
    navHostController: NavHostController
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
                items(data.data) { materia ->
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
                            MySmallParagraphFormat("Docente: ", materia.docente ?: "Sin asignar")
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
            openFeature(data.modulo, navHostController)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataFinanzas(
    data: ConsultaGeneralFinanza,
    navHostController: NavHostController
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
                if (data.data.isEmpty()) {
                    item {
                        MySmallParagraph("No tiene deudas pendientes")
                    }
                } else {
                    items(data.data) { rubro ->
                        MyCard (
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ){
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                MySmallTitle(rubro.nombre)
                                Spacer(Modifier.height(8.dp))
                                MySmallParagraphFormat("Fecha Vencimiento: ", rubro.fechaVence)
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
            openFeature(data.modulo, navHostController)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataAlumno(
    data: ConsultaGeneralAlumno,
    navHostController: NavHostController
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

            openFeature(data.modulo, navHostController)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun dataMatricula(
    data: ConsultaGeneralMatricula,
    navHostController: NavHostController
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
            openFeature(data.modulo, navHostController)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun openFeature(
    modulo: ConsultaGeneralModulo,
    navHostController: NavHostController
) {
    Spacer(Modifier.height(4.dp))
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        TextButton(
            onClick = {
                navHostController.navigate(modulo.url)
            }
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = modulo.descripcion,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.OpenInNew,
                    contentDescription = modulo.descripcion,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}