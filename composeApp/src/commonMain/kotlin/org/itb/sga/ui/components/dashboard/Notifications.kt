package org.itb.sga.ui.components.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.ROUTES
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.notificaciones.Notificacion
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.alerts.MyAlert
import org.itb.sga.ui.components.shimmer.ShimmerFormLoadingAnimation

@Composable
fun Notificacion(
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    val notificaciones by homeViewModel.notificaciones.collectAsState(emptyList())
    val showNotifications by homeViewModel.showNotifications.collectAsState(false)

    if (notificaciones.isNotEmpty() && showNotifications) {
        MyAlert(
            title = "Notificaciones ITB",
            onDismiss = {
                homeViewModel.changeShowNotifications(false)
            },
            showAlert = showNotifications,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            text = {
                LazyColumn {
                    items(notificaciones) { notificacion ->
                        NotificationItem(
                            notificacion = notificacion,
                            navController = navController,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NotificationDetail(
    notificacion: Notificacion
) {
    notificacion.detail?.let { detalleData ->
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(detalleData.detalle) { detalle ->
                MyCard {
                    Column(
                        modifier = Modifier.width(250.dp)
                    ) {
                        detalle.detalle_tabla.forEachIndexed { index, it ->
                            Text(
                                text = formatoText(detalleData.cabecera_tabla[index], it),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
            }
        }
    } ?: ShimmerFormLoadingAnimation(1)
}

@Composable
fun NotificationItem(
    notificacion: Notificacion,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = notificacion.notificacion_titulo,
                style = MaterialTheme.typography.titleSmall
            )
            if (notificacion.utiliza_detalle) {
                Spacer(Modifier.width(8.dp))
                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(
                        onClick = {
                            expanded = !expanded
                            if (expanded) {
                                homeViewModel.onloadNotificacionDetalle(notificacion)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
        }

        if (notificacion.notificacion_descripcion.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = notificacion.notificacion_descripcion,
                style = MaterialTheme.typography.bodySmall
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                NotificationDetail(notificacion)
            }
        }

        Spacer(Modifier.height(4.dp))
        notificacion.urls?.takeIf { it.isNotEmpty() }?.let { urls ->
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(urls) { index, url ->
                    val path = url.url
                    val cleanPath = if (path.startsWith("/")) path.substring(1) else path
                    if (cleanPath in ROUTES) {
                        MyFilledTonalButton(
                            text = url.name,
                            enabled = true,
                            shape = RoundedCornerShape(8.dp),
                            textStyle = MaterialTheme.typography.labelSmall,
                            onClickAction = {
                                navController.navigate(cleanPath)
                            },
                            buttonColor = when (url.tipo) {
                                "info" -> MaterialTheme.colorScheme.primaryContainer
                                "success" -> MaterialTheme.colorScheme.onPrimaryContainer
                                "danger" -> MaterialTheme.colorScheme.errorContainer
                                "warning" -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.surfaceContainer
                            },
                            textColor = when (url.tipo) {
                                "info" -> MaterialTheme.colorScheme.primary
                                "success" -> MaterialTheme.colorScheme.onPrimary
                                "danger" -> MaterialTheme.colorScheme.error
                                "warning" -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                        )
//
//
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            TextButton(
//                                onClick = {
//
//                                    navController.navigate(cleanPath)
//                                }
//                            ) {
//                                Text(
//                                    text = url.name,
//                                    style = MaterialTheme.typography.labelSmall,
//                                    color = MaterialTheme.colorScheme.tertiary
//                                )
//                            }
//                            if (index < urls.lastIndex) {
//                                Spacer(Modifier.width(4.dp))
//                                VerticalDivider(
//                                    modifier = Modifier
//                                        .height(24.dp)
//                                        .width(1.dp)
//                                        .align(Alignment.CenterVertically),
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                                Spacer(Modifier.width(4.dp))
//                            }
//                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        HorizontalDivider()
        Spacer(Modifier.height(4.dp))
    }
}