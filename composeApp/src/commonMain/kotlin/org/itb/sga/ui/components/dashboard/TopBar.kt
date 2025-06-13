package org.itb.sga.ui.components.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Sort
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import org.itb.sga.features.common.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String,
    drawerState: DrawerState,
    homeViewModel: HomeViewModel
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
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(68.dp)
            ) {
                Crossfade(targetState = onSearch) { isSearch ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
//                            .padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (!isSearch) {
                            var searchQuery by remember { mutableStateOf("") }

                            TextField(
                                value = searchQuery,
                                onValueChange = { newQuery ->
                                    searchQuery = newQuery
                                    if (homeViewModel.fastSearch.value) {
                                        homeViewModel.onSearchQueryChanged(newQuery)
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                colors = TextFieldDefaults.colors(
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                label = { Text("Buscar") },
                                singleLine = true,
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
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
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
                    imageVector = Icons.Filled.Sort,
                    contentDescription = "Menú lateral",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },

    )
}

