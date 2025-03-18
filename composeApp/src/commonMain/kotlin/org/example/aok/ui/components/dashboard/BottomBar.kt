package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.features.common.home.HomeViewModel

@Composable
fun MyBottomBar(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val showNotifications by homeViewModel.showNotifications.collectAsState(false)
    val notificaciones by homeViewModel.notificaciones.collectAsState(emptyList())

    Surface (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .height(96.dp)
    ){
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            NavigationBarItem(
                selected = false, // Puedes cambiar esto según la navegación activa
                onClick = {
                    navController.navigate("home")
                    homeViewModel.clearSearchQuery()
                },
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") }
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    homeViewModel.changeBottomSheet()
                },
                icon = { Icon(Icons.Filled.Menu, contentDescription = "Periodos") },
                label = { Text("Períodos") }
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate("account")
                },
                icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                label = { Text("Perfil") }
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    homeViewModel.changeShowNotifications(!showNotifications)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (notificaciones.size > 0) {
                                Badge { Text(notificaciones.size.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                label = {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            )
        }
    }
}