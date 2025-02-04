package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.features.common.home.HomeViewModel

@Composable
fun MyBottomBar(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomBarItem(
                    icon = Icons.Filled.Home,
                    label = "Inicio",
                    onClick = {
                        navController.navigate("home")
                        homeViewModel.clearSearchQuery()
                    }
                )
                BottomBarItem(
                    icon = Icons.Filled.Menu,
                    label = "Periodos",
                    onClick = {
                        homeViewModel.changeBottomSheet()
                    }
                )
                BottomBarItem(
                    icon = Icons.Filled.Person,
                    label = "Perfil",
                    onClick = {
                        navController.navigate("account")
                    }
                )
                BottomBarItem(
                    icon = Icons.Filled.Notifications,
                    label = "Notificaciones",
                    onClick = {

                    }
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

