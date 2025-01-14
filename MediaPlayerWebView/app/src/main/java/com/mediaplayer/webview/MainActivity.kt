package com.mediaplayer.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mediaplayer.webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialize()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initialize() {
        WebView.setWebContentsDebuggingEnabled(true)

        binding.webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = false
            loadsImagesAutomatically = true
            defaultTextEncodingName = "utf-8"
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            mediaPlaybackRequiresUserGesture = false
        }

        binding.webView.webViewClient = WebClient()

        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Web View Video</title>
            </head>
            <body>
                <video width="100%" height="auto" controls autoplay loop>
                    <source src="https://static.obemannadbutik.se/undefined/images/Coca%20Cola.mp4" type="video/mp4"
                </video>
            </body>
            </html>
        """
        Log.e("MainActivity", "Loading HTML content")
        binding.webView.loadData(htmlContent, "text/html", "UTF-8")
    }

    inner class WebClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.e("WebClient", "onPageStarted: $url")
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.e("WebClient", "shouldOverrideUrlLoading: $url")
            view?.loadUrl(url.toString())
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.e("WebClient", "onPageFinished: $url")
            binding.webView.stopLoading()
        }
    }
}
