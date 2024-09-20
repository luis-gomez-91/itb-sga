//package org.example.aok.ui.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectVerticalDragGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//
//@Composable
//fun MyPageRefresh(
//    onRefresh: @Composable () -> Unit,
//    isRefreshing: Boolean,
//    content: @Composable () -> Unit
//) {
//    var offsetY by remember { mutableStateOf(0f) }
//    val scope = rememberCoroutineScope()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(Unit) {
//                detectVerticalDragGestures(
//                    onVerticalDrag = { _, dragAmount ->
//                        offsetY += dragAmount
//                    },
//                    onDragEnd = {
//                        if (offsetY > 100) { // Detecta que el usuario arrastró hacia abajo
//                            scope.launch {
//                                onRefresh() // Llama a la función de refrescar
//                            }
//                        }
//                        offsetY = 0f // Restablece el estado
//                    }
//                )
//            }
//    ) {
//        Column(
//            modifier = Modifier
//                .offset(y = offsetY.dp)
//                .fillMaxSize()
//        ) {
//            if (isRefreshing) {
//                // Mostrar un indicador de carga mientras refresca
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .padding(16.dp)
////                        .align(Alignment.CenterHorizontally)
//                )
//            }
//            // Contenido principal
//            content()
//        }
//    }
//}
