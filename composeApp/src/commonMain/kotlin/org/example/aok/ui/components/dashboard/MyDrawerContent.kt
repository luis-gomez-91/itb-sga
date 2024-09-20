package org.example.aok.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.facebook
import aok.composeapp.generated.resources.instagram
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import aok.composeapp.generated.resources.tiktok
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.common.login.RedesSociales
import org.example.aok.ui.components.MyAssistChip
import org.jetbrains.compose.resources.painterResource

@Composable
fun MyDrawerContent(
    drawerState: DrawerState,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
//    val homeDataState = homeViewModel.homeData.observeAsState()
//    val persona = homeDataState.value?.persona
//
    val items = listOf(
        ItemsDrawer("Perfil", Icons.Filled.Person, "account"),
        ItemsDrawer("Logout", Icons.Filled.Logout, "logout"),
        ItemsDrawer("Cambiar contraseña", Icons.Filled.LockReset, "")
    )

    val imageLogo =
        if (isSystemInDarkTheme()) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }

//    val defaultPainter: Painter = painterResource(id = R.drawable.logo)

    ModalDrawerSheet(

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                MaterialTheme.colorScheme.tertiaryContainer,
//                                Color.Transparent
//                            )
//                        )
//                    )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(imageLogo),
                        contentDescription = "logo",
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }

                Text(
                    text = "Sistema de Gestión Académica",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

//                Text(
//                    text = homeViewModel.homeData.value?.persona?.nombre ?: "",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    color = MaterialTheme.colorScheme.secondary
//                )
//                Text(
//                    text = homeViewModel.homeData.value?.persona?.identificacion ?: "",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colorScheme.secondary
//                )
//
//                Row {
//                    MyAssistChip(
//                        label = homeViewModel.homeData.value?.persona?.emailinst ?: "",
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        labelColor = MaterialTheme.colorScheme.tertiary,
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    MyAssistChip(
//                        label = homeViewModel.homeData.value?.persona?.usuario ?: "",
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        labelColor = MaterialTheme.colorScheme.tertiary,
//                    )
//                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label, fontSize = 12.sp) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            if (item.label == "Logout") {
//                                loginViewModel.logout(navHostController, context)
                            } else {
//                                navHostController.navigate(item.navigate)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }

            RedesSociales()
        }
    }
}


data class ItemsDrawer(
    val label: String,
    val icon: ImageVector,
    val navigate: String,
)