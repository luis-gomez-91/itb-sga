package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.features.common.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel
//    perfilViewModel: PerfilViewModel
) {
    Surface (
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ){
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
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
                    icon = Icons.Filled.ViewList,
                    label = "Periodos",
                    onClick = {  }
                )
                BottomBarItem(
                    icon = Icons.Filled.Person,
                    label = "Perfil",
                    onClick = {
                        navController.navigate("account")
                    }
                )
            }

            if (false) {
//            ModalBottomSheet(
//                onDismissRequest = { mainViewModel.showBottomSheet = false }
//            ) {
//                ContentBottomSheet(navController, homeViewModel, perfilViewModel)
//            }
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalNavigationDrawer(
                            drawerContent = {
                                ModalDrawerSheet {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                    ) {
                                        Text(text = "Header", modifier = Modifier.padding(16.dp))
                                    }
                                    Divider()
                                    NavigationDrawerItem(
                                        label = { Text(text = "Mi perfil") },
                                        selected = false,
                                        icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Perfil", tint = MaterialTheme.colorScheme.tertiaryContainer) },
                                        onClick = { /* Handle profile click */ }
                                    )
                                }
                            },
//                        onDismissRequest = { mainViewModel.showNavigationDrawer = false }
                        ) {
                            // Your main content here
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(text = "Main Content")
                            }
                        }
                    },
//                onDismissRequest = { showNavigationDrawer = false }
                ) {
                    // Your main content here
                }
            }

//        Opciones de icono Periodos
            if (false) {
                ModalBottomSheet(
                    onDismissRequest = {  }
                ) {
                    PeriodosContentSheet(homeViewModel)
                }
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp).weight(0.7f),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(0.3f)
        )
    }
}

@Composable
fun PeriodosContentSheet(homeViewModel: HomeViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
//        homeViewModel.homeData.value?.let { data ->
//            items(data.periodos) { periodo ->
//                PeriodoItem(periodo)
//            }
//        }

    }
}

//@Composable
//fun PeriodoItem(periodo: Periodo) {
//    TextButton(
//        modifier = Modifier
//            .padding(vertical = 4.dp, horizontal = 4.dp)
//            .fillMaxWidth(),
//        onClick = {},
//        colors = ButtonDefaults.textButtonColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        )
//    ) {
//        Text(
//            text = periodo.nombre,
//            fontSize = 12.sp,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//    }
//}

