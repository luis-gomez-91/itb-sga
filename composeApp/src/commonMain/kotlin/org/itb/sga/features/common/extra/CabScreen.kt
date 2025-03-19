package org.itb.sga.features.common.extra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.dashboard.DashBoardScreen

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
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
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
            recorrerLista(beneficios)

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
            recorrerLista(socios)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Requisitos",
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
            recorrerLista(requisitos)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Financiamiento",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            val financiamiento = listOf(
                "Hasta 5 veces el valor de ahorros en certificados de aportación.",
                "Financiamiento máximo hasta por un nivel de su programa de estudios.",
                "Plazo Máximo 12 meses.",
                "Tasa Preferencial del 9,11% anual.",
                "Aporte al fondo de fortalecimiento patrimonial del 2% del monto aprobado.",
                "Garantía: Garante Personal."
            )
            recorrerLista(financiamiento)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¿Quiénes somos?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "La CAB, es una entidad formada por personas naturales cuyo vinculo común es pertenecer a la comunidad Educativa Bolivariana, conformada por estudiantes y colaboradores del Instituto Superior Universitario de Tecnología y la Universidad Bolivariana del Ecuador, unidas voluntariamente que se forma con aportes económicos de sus socios, en calidad de ahorros, sin que pueda captar fondos de terceros, para el otorgamiento de créditos a sus miembros de conformidad con la Ley Orgánica de Economía Popular y Solidaria, el Reglamento a la Ley Orgánica de Economía Popular y Solidaria, el Código Orgánico Monetario y Financiero, el Estatuto Social de la caja de ahorro, las regulaciones y resoluciones emitidas por la Junta de Política y Regulación Monetaria y Financiera y la Superintendencia de Economía Popular y Solidaria.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Misión",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Captar ahorros de los socios, quienes cumplan con el vínculo común, para atenderlos con servicios financieros integrales con beneficios en costo, fortaleciendo así sus competencias financieras.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Visión",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Ser un referente de apoyo e innovación financiera para la comunidad Bolivariana; y mediante la aplicación de buenas prácticas de gobierno, convertirnos en una Cooperativa de Ahorro y Crédito abierta.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun recorrerLista(list: List<String>) {
    list.forEach {
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