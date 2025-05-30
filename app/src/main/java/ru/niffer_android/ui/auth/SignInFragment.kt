package ru.niffer_android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import ru.niffer_android.databinding.FragmentSignInBinding
import androidx.navigation.fragment.findNavController
import ru.niffer_android.R

@AndroidEntryPoint
class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel: SignInViewModel by activityViewModels()

    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val dataIntent = it.data ?: return@registerForActivityResult
            val isCodeReceived = handleAuthorizationResponse(dataIntent)
            if (isCodeReceived) {
                findNavController().navigate(R.id.action_signInFragment_to_postLoginFragment)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInViewModel.apply {
            restoreState()
        }

        binding.buttonLogin.setOnClickListener {
            startAuthorization()
        }
    }

    private fun startAuthorization() {
        val authIntent = signInViewModel.startAuthorization()
        getAuthResponse.launch(authIntent)
    }

    private fun handleAuthorizationResponse(intent: Intent): Boolean {
        val authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        signInViewModel.setAuthState(authorizationResponse, error)

        if (authorizationResponse != null) {
            signInViewModel.exchangeAuthorizationCode(authorizationResponse, requireActivity())
            return true
        }
        return false
    }
}