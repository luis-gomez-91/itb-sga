package org.itb.sga.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.data.network.PagingBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen2(
    content: @Composable () -> Unit = {},
    title: String = "",
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
//                colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
//                ),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                title = { Text(title) },
                modifier = Modifier,
                actions = {}
//                windowInsets = {},
//                scrollBehavior = {},
            )
        }
//        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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