package ru.niffer_android.ui.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient

import androidx.fragment.app.Fragment

import dagger.hilt.android.AndroidEntryPoint
import ru.niffer_android.databinding.FragmentMainBinding
import ru.niffer_android.network.WebAppInterface

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }


    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(), "AndroidInterface")
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.setSupportMultipleWindows(false)
            settings.useWideViewPort = true
            settings.userAgentString += " NifflerAndroid"
            webViewClient = object : WebViewClient() {}
            loadUrl("https://niffler-stage.qa.guru/")
        }
        Log.d("WEBVIEW USER AGENT",binding.webView.settings.userAgentString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}