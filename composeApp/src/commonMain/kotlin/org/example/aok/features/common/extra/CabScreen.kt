package org.example.aok.features.common.extra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun CabScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    DashBoardScreen(
        title = "Caja de Ahorro Bolivariano",
        navController = navController,
        content = { Screen() },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "La caja de ahorro promueve el desarrollo profesional y personal, por ello como entidad financiera nos encontramos comprometidos con la profesionalización de la comunidad Bolivariana, brindadores asistencia financiera para que puedan cumplir sus metas.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Beneficios",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val beneficios = listOf(
            "Rendimiento hasta del 20% sobre sus ahorros en certificados de aportación, por cada pago puntual de sus pensiones al ITB-U aportará con 1 usd a su certificado de aportación; es decir al término de cada semestre podrá contar con \$30 de aportacion individual + \$6 usd de aportacion del ITB-U; permitiendo asi reunir mayores garantías para optar por financiamiento emergente para cubrir cuotas dentro de un nivel de estudios.",
            "Podrán acceder a un descuento del 10% por pago anticipado del nivel",
            "Descuento en el GYM del 10%",
            "Descuento en cuotas de los modulos de inglés del 10%.",
            "Descuento en atención en ASOMI del 10% en sus servicios"
        )

        beneficios.forEach {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¿Cómo ser socio?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val socios = listOf(
            "Constar como estudiante Inscrito en cualquiera de los programas del ITB-U y llenar una solicitud donde debe completar datos personales.",
            "Para mantener la calidad de socio deberá abonar mensualmente al menos \$3 usd, ya sea en la cuenta cte de la CAB o en las ventanillas de cobro de la UBE o ITB-U.",
            "El aporte mensual le pertenece a cada uno de los socios y se registrarán en el certificado de aportación individual quienes tienen la posibilidad de retirarlos al término de cada semestre hasta el 100%, siempre que no tenga obligaciones y con carta de notificación de 30 días previos, caso contario se renovará automáticamente por un semestre adicional."
        )

        socios.forEach {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¿Cómo ser socio?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val requisitos = listOf(
            "Ser socio de la caja de ahorro.",
            "Llenar formulario de solicitud de crédito",
            "Planilla de servicios básicos actualizada (luz, agua o teléfono).",
            "Roles de pago (tres últimos sellados por la institución).",
            "Certificado laboral que justifique un año de estabilidad.",
            "Historia Laboral del IESS",
            "Documentos que respalden Bienes (Copia Simple de Predios, matrícula de vehículo, otros).",
            "En caso de contar con actividad independiente (tres últimas declaraciones del IVA y último formulario de impuesto a la renta)",
        )

        requisitos.forEach {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


    }
}