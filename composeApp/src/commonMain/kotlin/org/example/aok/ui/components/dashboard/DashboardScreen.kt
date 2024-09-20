package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

@Composable
fun DashBoardScreen(
    title: String,
    navController: NavHostController,
    content: @Composable () -> Unit = {},
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val context = LocalContext.current
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawerContent(
                drawerState,
                navController,
                homeViewModel,
                loginViewModel
            )
        },
        content = {
            Scaffold(
                topBar = { MyTopBar(title = title, drawerState = drawerState, homeViewModel = homeViewModel, mainViewModel = mainViewModel) },
                bottomBar = { MyBottomBar(navController = navController, homeViewModel = homeViewModel, mainViewModel = mainViewModel) }
            ) { innerPadding ->
                Surface(
                    Modifier
                        .padding(innerPadding)
//                        .padding(vertical = 4.dp, horizontal = 4.dp)
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 4.dp)
                    ) {
                        content()
                    }
                }
            }
        }
    )
}