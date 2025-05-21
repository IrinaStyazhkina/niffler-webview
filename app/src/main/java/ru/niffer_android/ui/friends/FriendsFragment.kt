package ru.niffer_android.ui.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.niffer_android.R
import ru.niffer_android.adapter.people.OnInteractionListener
import ru.niffer_android.adapter.people.PeopleAdapter
import ru.niffer_android.databinding.FragmentFriendsBinding
import ru.niffer_android.model.Result
import ru.niffer_android.network.WebAppInterface
import ru.niffer_android.ui.bottomSheet.SubmitBottomSheet
import ru.niffer_android.ui.bottomSheet.SubmitButtonStyle
import ru.niffer_android.utils.hideLoader
import ru.niffer_android.utils.showError
import ru.niffer_android.utils.showLoader

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