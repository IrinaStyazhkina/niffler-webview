package ru.niffer_android.ui.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.niffer_android.R
import ru.niffer_android.adapter.currency.CurrencyDropdownAdapter
import ru.niffer_android.adapter.spending.OnInteractionListener
import ru.niffer_android.adapter.spending.SpendingAdapter
import ru.niffer_android.databinding.FragmentMainBinding
import ru.niffer_android.model.Currency
import ru.niffer_android.model.CurrencyDropdownItem
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Period
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.model.Statistics
import ru.niffer_android.ui.newSpending.EditSpendingViewModel
import ru.niffer_android.utils.AndroidUtils
import ru.niffer_android.utils.CHART_COLORS
import ru.niffer_android.utils.ColorUtils
import ru.niffer_android.utils.CurrencyUtils
import ru.niffer_android.utils.UiFormatter
import ru.niffer_android.utils.hideLoader
import ru.niffer_android.utils.showError
import ru.niffer_android.utils.showLoader
import ru.niffer_android.storage.TokenStorage
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    request?.let {
                        if (it.url.host == "api.niffler-stage.qa.guru") {
                            val token = TokenStorage.idToken
                            if (token != null) {
                                Log.d("API TOKEN", token)
                                it.requestHeaders["Authorization"] = "Bearer $token"
                            }
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
                }
            }
            loadUrl("https://niffler-stage.qa.guru/")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}