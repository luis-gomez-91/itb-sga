package org.itb.sga.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Home
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.outline.Bell
import compose.icons.evaicons.outline.Home
import compose.icons.evaicons.outline.Menu
import compose.icons.evaicons.outline.Person
import org.itb.sga.data.domain.BottomBarItem
import org.itb.sga.features.common.home.HomeViewModel

@Composable
fun MyBottomBar(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val showNotifications by homeViewModel.showNotifications.collectAsState(false)
    val notificaciones by homeViewModel.notificaciones.collectAsState(emptyList())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isHomeSelected = currentRoute == "home"
    val isProfileSelected = currentRoute == "account"
    val data by homeViewModel.homeData.collectAsState(null)

    val navigationIcons = buildList {
        add(
            BottomBarItem(
                onclick = {
                    navController.navigate("home")
                    homeViewModel.clearSearchQuery()
                },
                label = "Inicio",
                icon = if (isHomeSelected) EvaIcons.Fill.Home else EvaIcons.Outline.Home,
                color = if (isHomeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                isSelected = isHomeSelected
            )
        )

        add(
            BottomBarItem(
                onclick = { navController.navigate("account") },
                label = "Perfil",
                icon = if (isProfileSelected) EvaIcons.Fill.Person else EvaIcons.Outline.Person,
                color = if (isProfileSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                isSelected = isProfileSelected
            )
        )

        if (data?.persona?.esInscripcion == false) {
            add(
                BottomBarItem(
                    onclick = { homeViewModel.changeBottomSheet() },
                    label = "Períodos",
                    icon = EvaIcons.Outline.Menu,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    isSelected = false
                )
            )
        }
    }

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
            navigationIcons.forEach {
                NavigationBarItem(
                    selected = it.isSelected,
                    onClick = { it.onclick() },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = it.label,
                            style = it.style,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }

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
                            imageVector = EvaIcons.Outline.Bell,
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