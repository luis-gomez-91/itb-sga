package org.example.aok.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.facebook
import aok.composeapp.generated.resources.instagram
import aok.composeapp.generated.resources.tiktok
import org.example.aok.features.common.home.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun SocialMedia(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    val redes = listOf(
        Pair(Res.drawable.instagram, "https://www.instagram.com/itb_ec"),
        Pair(Res.drawable.facebook, "https://www.facebook.com/itb.edu.ec"),
        Pair(Res.drawable.tiktok, "https://www.tiktok.com/@itb_ec?lang=es")
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        redes.forEach { (icono, url) ->
            IconButton(onClick = { homeViewModel.openURL(url) }) {
                Image(
                    painter = painterResource(icono),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}