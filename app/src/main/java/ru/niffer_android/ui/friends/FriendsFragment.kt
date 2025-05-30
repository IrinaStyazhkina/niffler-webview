package ru.niffer_android.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.niffer_android.databinding.FragmentFriendsBinding
import ru.niffer_android.network.WebAppInterface


@AndroidEntryPoint
class FriendsFragment: Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(
            inflater,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    private fun setupWebView() {
        binding.friendsWebView.apply {
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
            loadUrl("https://niffler-stage.qa.guru/people/friends")
        }
    }

}