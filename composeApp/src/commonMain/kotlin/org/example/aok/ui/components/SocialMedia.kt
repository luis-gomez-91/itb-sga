package org.example.aok.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.facebook
import aok.composeapp.generated.resources.instagram
import aok.composeapp.generated.resources.tiktok
import coil3.compose.AsyncImage
import org.example.aok.core.SERVER_URL
import org.example.aok.features.common.home.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun SocialMedia(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    size: Dp
) {
//    val redes = listOf(
//        Pair(Res.drawable.instagram, "https://www.instagram.com/itb_ec"),
//        Pair(Res.drawable.facebook, "https://www.facebook.com/itb.edu.ec"),
//        Pair(Res.drawable.tiktok, "https://www.tiktok.com/@itb_ec?lang=es")
//    )

    val redes = listOf(
        Pair("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1200px-Instagram_icon.png", "https://www.instagram.com/itb_ec"),
        Pair("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/2021_Facebook_icon.svg/2048px-2021_Facebook_icon.svg.png", "https://www.facebook.com/itb.edu.ec"),
        Pair("https://upload.wikimedia.org/wikipedia/commons/e/ef/Youtube_logo.png", "https://www.youtube.com/@ITBUniversitario"),
        Pair("https://static.wikia.nocookie.net/logopedia/images/0/08/TikTok_icon.svg/revision/latest/scale-to-width-down/250?cb=20201203151915&path-prefix=es", "https://www.tiktok.com/@itb_ec?lang=es"),
        Pair("https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/LinkedIn_icon.svg/1200px-LinkedIn_icon.svg.png", "https://www.linkedin.com/company/itb-ec/posts/?feedView=all")
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
//        redes.forEach { (icono, url) ->
//            IconButton(onClick = { homeViewModel.openURL(url) }) {
//                Image(
//                    painter = painterResource(icono),
//                    contentDescription = null,
//                    modifier = Modifier.size(80.dp)
//                )
//            }
//        }
        redes.forEach { (icono, url) ->
            AsyncImage(
                model = icono,
                contentDescription = "Social Media",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(size)
                    .clickable { homeViewModel.openURL(url) }
            )
        }


    }


}