package org.itb.sga.features.payroll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun PayrollScreen(

) {
    val webViewState = rememberWebViewState(url = "http://10.10.9.25:89")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        WebView(
            state = webViewState
        )
    }
}