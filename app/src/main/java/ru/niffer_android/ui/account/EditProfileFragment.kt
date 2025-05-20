package ru.niffer_android.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.niffer_android.databinding.FragmentEditProfileBinding
import ru.niffer_android.model.Result
import ru.niffer_android.ui.bottomSheet.EditTextBottomSheet
import ru.niffer_android.utils.loadBase64Image

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val accountViewModel: AccountViewModel by activityViewModels()
    private var userFullName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(
            inflater,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            accountViewModel.user.collect { userData ->
                if (userData is Result.Success) {
                    binding.tvNameValue.text = userData.data.fullname
                    binding.tvUsernameValue.text = userData.data.username
                    userFullName = userData.data.fullname
                    if (userData.data.photo != null) {
                        binding.ivProfileImage.loadBase64Image(
                            userData.data.photo
                        )
                    }
                }
            }
        }

        binding.profileDataContainer.setOnClickListener {
            val modalBottomSheet = EditTextBottomSheet(labelText = "Edit name", inputText = userFullName ?: "")
                .setHint(hint = "Type your name")
                .setOnSaveClickListener { name ->
                    accountViewModel.updateUser(name)
                }

            modalBottomSheet.show(parentFragmentManager, EditTextBottomSheet.TAG)
        }

        binding.photoEditButton.setOnClickListener {
            val modalBottomSheet = EditAvatarBottomSheet(accountViewModel)
            modalBottomSheet.show(parentFragmentManager, EditAvatarBottomSheet.TAG)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}