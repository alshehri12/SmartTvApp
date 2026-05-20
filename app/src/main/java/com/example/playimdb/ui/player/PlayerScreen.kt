package com.example.playimdb.ui.player

import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.playimdb.Constants

@Composable
fun PlayerScreen(
    imdbId: String,
    onBackClick: () -> Unit
) {
    val url = "${Constants.PLAYIMDB_BASE_URL}$imdbId"
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var customView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    BackHandler {
        when {
            customView != null -> {
                customViewCallback?.onCustomViewHidden()
                customView = null
                customViewCallback = null
            }
            webViewRef?.canGoBack() == true -> webViewRef?.goBack()
            else -> onBackClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        allowFileAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        userAgentString = "Mozilla/5.0 (Linux; Android 11; TV) AppleWebKit/537.36 Chrome/91.0.4472.120 Safari/537.36"
                        // Route window.open() and target="_blank" through onCreateWindow
                        // so we can block them there without touching normal navigation
                        setSupportMultipleWindows(true)
                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest
                        ): Boolean {
                            // Allow all normal navigation so the video player loads correctly
                            view.loadUrl(request.url.toString())
                            return true
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onCreateWindow(
                            view: WebView,
                            isDialog: Boolean,
                            isUserGesture: Boolean,
                            resultMsg: android.os.Message?
                        ): Boolean {
                            // Block every popup / new-tab request (ad redirects on video click)
                            // This is the ONLY entry point for window.open() and target="_blank"
                            return false
                        }

                        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                            customView = view
                            customViewCallback = callback
                        }

                        override fun onHideCustomView() {
                            customViewCallback?.onCustomViewHidden()
                            customView = null
                            customViewCallback = null
                        }
                    }

                    webViewRef = this
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Fullscreen video overlay
        customView?.let { cv ->
            AndroidView(
                factory = { cv },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }
}
