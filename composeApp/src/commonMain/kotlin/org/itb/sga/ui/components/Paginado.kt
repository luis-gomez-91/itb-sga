package org.itb.sga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.itb.sga.data.network.Paging
import org.itb.sga.features.common.home.HomeViewModel

@Composable
fun Paginado(
    isLoading: Boolean,
    paging: Paging,
    homeViewModel: HomeViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val actualPage by homeViewModel.actualPage.collectAsState(1)

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                onBack()
            },
            enabled = actualPage > 1  && !isLoading
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "Back",
                tint = if (actualPage > 1  && !isLoading) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        }

        Text(
            text = "${actualPage}/${paging.lastPage}",
            style = MaterialTheme.typography.titleLarge,
            color = if (!isLoading) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
        )

        IconButton(
            onClick = {
                onNext()
            },
            enabled = actualPage < (paging.lastPage) && !isLoading
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = if (actualPage < (paging.lastPage)  && !isLoading) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}