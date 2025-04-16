package org.itb.sga.core

import androidx.compose.runtime.Composable

@Composable
expect fun MyWebView(
    htmlContent: String,
    isLoading: (isLoading: Boolean) -> Unit,
    onUrlClicked: (url: String) -> Unit
)