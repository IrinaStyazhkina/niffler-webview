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
import ru.niffer_android.R
import ru.niffer_android.adapter.category.CategoryAdapter
import ru.niffer_android.adapter.category.OnInteractionListener
import ru.niffer_android.databinding.FragmentAccountBinding
import ru.niffer_android.model.Category
import ru.niffer_android.model.Result
import ru.niffer_android.model.User
import ru.niffer_android.ui.bottomSheet.SubmitBottomSheet
import ru.niffer_android.ui.bottomSheet.EditTextBottomSheet
import ru.niffer_android.utils.hideLoader
import ru.niffer_android.utils.loadBase64Image
import ru.niffer_android.utils.showError
import ru.niffer_android.utils.showLoader

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountViewModel: AccountViewModel by activityViewModels()
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesAdapter = CategoryAdapter(object : OnInteractionListener {
            override fun onArchiveButtonClick(category: Category) {
                val modalBottomSheet = if (!category.archived) {
                    SubmitBottomSheet(
                        titleText = getString(R.string.archive_category),
                        subtitleText = getString(R.string.do_you_really_want_to_archive, category.name),
                        helperText = getString(R.string.after_this_change_it_won_t_be_available_while_creating_spends)
                    )
                        .setSubmitButtonText(getString(R.string.archive))
                        .setOnSaveClickListener {
                            categoriesViewModel.updateCategory(category.copy(archived = true))
                        }
                } else {
                    SubmitBottomSheet(
                        titleText = getString(R.string.unarchive_category),
                        subtitleText = getString(R.string.do_you_really_want_to_unarchive, category.name),
                    )
                        .setSubmitButtonText(getString(R.string.unarchive))
                        .setOnSaveClickListener {
                            categoriesViewModel.updateCategory(category.copy(archived = false))
                        }
                }
                modalBottomSheet.show(parentFragmentManager, SubmitBottomSheet.TAG)
            }

            override fun onEditClick(category: Category) {
                val modalBottomSheet = EditTextBottomSheet(
                    labelText = getString(R.string.edit_category),
                    inputText = category.name,
                    validateNotBlank = true,
                )
                    .setHint(getString(R.string.type_something))
                    .setOnSaveClickListener { categoryName ->
                        if (categoryName.isNotEmpty())
                            categoriesViewModel.updateCategory(
                                category.copy(name = categoryName)
                            )
                    }
                modalBottomSheet.show(parentFragmentManager, EditTextBottomSheet.TAG)
            }
        })

        binding.categoriesLayout.categoriesList.adapter = categoriesAdapter

        lifecycleScope.launch {
            accountViewModel.user.collect { userData ->
                when (userData) {
                    is Result.Loading -> showLoader()
                    is Result.Success -> {
                        hideLoader()
                        renderProfileData(userData.data)
                    }

                    is Result.Error -> {
                        hideLoader()
                        showError("Profile data is not loaded")
                    }
                }
            }
        }

        lifecycleScope.launch {
            categoriesViewModel.categories.collect { categoriesData ->
                when (categoriesData) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        categoriesAdapter.submitList(categoriesData.data)
                    }

                    is Result.Error -> {
                        showError("Categories are not loaded")
                    }
                }
            }
        }

        binding.categoriesLayout.categoriesSwitch.setOnClickListener {
            val excludeArchived = !binding.categoriesLayout.categoriesSwitch.isChecked
            categoriesViewModel.loadCategories(excludeArchived)
        }

        binding.categoriesLayout.buttonNewCategory.setOnClickListener {
            val modalBottomSheet = EditTextBottomSheet(
                labelText = "New category",
                inputText = "",
                validateNotBlank = true
            )
                .setHint("Type category name")
                .setOnSaveClickListener { categoryName ->
                    if (categoryName.isNotEmpty())
                        categoriesViewModel.createCategory(categoryName)
                }
            modalBottomSheet.show(parentFragmentManager, EditTextBottomSheet.TAG)
        }

        binding.profileLayout.profileEditButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
        }
    }

    private fun renderProfileData(user: User) {
        val username = "@${user.username}"
        binding.profileLayout.tvUsername.text = username
        binding.profileLayout.tvFullName.text = user.fullname ?: ""
        if (user.photo != null) {
            binding.profileLayout.ivProfileImage.loadBase64Image(
                user.photo
            )
        }
    }

}