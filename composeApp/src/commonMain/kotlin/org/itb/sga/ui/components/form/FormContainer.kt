package org.itb.sga.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.dashboard.MyBottomBar

@Composable
fun FormContainer(
    title: String,
    navController: NavHostController,
    content: @Composable () -> Unit = {},
    homeViewModel: HomeViewModel
) {
    Scaffold(
        topBar = {
            FormTopBar(
                title = title,
                navController = navController
            )
        },
        bottomBar = { MyBottomBar(navController = navController, homeViewModel = homeViewModel) }
    ) { innerPadding ->
        Surface(
            Modifier
                .padding(innerPadding)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTopBar(
    title: String,
    navController: NavHostController,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().height(68.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },

//        colors = TopAppBarDefaults.smallTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
//        ),

        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Menú lateral",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
    )
}
