package org.example.aok.ui.components.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.aok.core.MainViewModel
import org.example.aok.features.common.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MyTopBar(
    title: String,
    drawerState: DrawerState,
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel
) {
    val focusRequester = remember { FocusRequester() }
    var onSearch by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    if (!onSearch) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().height(68.dp)
            ) {
                Crossfade(targetState = onSearch) { isSearch ->
                    if (!onSearch) {
                        var searchQuery by remember { mutableStateOf("") }

                        TextField(
                            value = searchQuery,
                            onValueChange = { newQuery ->
                                searchQuery = newQuery
                                if (homeViewModel.fastSearch.value) {
                                    homeViewModel.onSearchQueryChanged(newQuery)
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(text = "Ingrese texto aqui...")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .focusRequester(focusRequester),
                            label = { Text("Buscar") },


                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text
                            ),

                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    homeViewModel.actualPageRestart()
                                    homeViewModel.onSearchQueryChanged(searchQuery)
                                    focusRequester.freeFocus()
                                }
                            )
                        )
                    } else {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
//                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },

        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        actions = {
            AnimatedContent(targetState = onSearch) { isSearch ->
                IconButton(
                    onClick = {
                        onSearch = !onSearch
                    }
                ) {
                    Icon(
                        imageVector = if (isSearch) Icons.Filled.Search else Icons.Filled.SearchOff,
                        contentDescription = if (isSearch) "Activar búsqueda" else "Desactivar búsqueda",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        },
        navigationIcon = {
            IconButton(
                onClick = { scope.launch { drawerState.open() } }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú lateral",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },

    )
}

