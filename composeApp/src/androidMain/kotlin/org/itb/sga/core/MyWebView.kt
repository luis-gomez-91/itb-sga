// MyWebView.kt (específica de Android)
package org.itb.sga.core

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun MyWebView(
    htmlContent: String,
    isLoading: (isLoading: Boolean) -> Unit,
    onUrlClicked: (url: String) -> Unit
) {
    var isLoadingFinished by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        // WebView with a loading indicator
        if (!isLoadingFinished) {
            CircularProgressIndicator(
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                WebView(context).apply {
                    scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
                    setBackgroundColor(Color.Transparent.toArgb())
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    // Enable horizontal scrolling
                    settings.apply {
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                    }

                    // Handle page load events
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            isLoading(true)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            view?.scrollTo(view.contentHeight, 0)
                            isLoadingFinished = true
                            isLoading(false)
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            return if (request?.url.toString().contains("jpg") ||
                                request?.url.toString().contains("png") ||
                                request?.url.toString().contains("attachment_id")
                            ) {
                                true // Handle images and attachments outside the WebView
                            } else {
                                onUrlClicked(request?.url.toString()) // Handle URL click
                                true
                            }
                        }
                    }
                }
            },
            update = { webView ->
                // Load HTML content into WebView
                webView.loadDataWithBaseURL(
                    "",
                    htmlContent,
                    "text/html",
                    "UTF-8",
                    ""
                )
            }
        )
    }
}
