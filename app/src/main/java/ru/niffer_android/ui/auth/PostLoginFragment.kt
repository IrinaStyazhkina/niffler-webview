package ru.niffer_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthState
import ru.niffer_android.R
import ru.niffer_android.activity.MainActivity
import ru.niffer_android.databinding.FragmentPostLoginBinding

@AndroidEntryPoint
class PostLoginFragment: Fragment()  {

    private lateinit var binding: FragmentPostLoginBinding
    private val signInViewModel: SignInViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostLoginBinding.inflate(
            inflater,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInViewModel.authLoadingLiveData.observe(viewLifecycleOwner) {
            if(!it.isLoading && it.isSuccessful) {
                navigate(signInViewModel.authStateLiveData.value)
            }
        }
    }


    private fun navigate(authState: AuthState?) {
        if (authState != null) {
            if (authState.isAuthorized) {
                signInViewModel.setAuthState(null, null)
                if (activity is StartActivity) {
                    findNavController().navigate(R.id.mainActivity)
                } else if (activity is MainActivity) {
                    findNavController().navigate(R.id.mainFragment)
                }
            }
        }
    }
}