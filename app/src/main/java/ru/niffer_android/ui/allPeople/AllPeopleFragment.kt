package ru.niffer_android.ui.allPeople

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.niffer_android.R
import ru.niffer_android.adapter.people.OnInteractionListener
import ru.niffer_android.adapter.people.PeopleAdapter
import ru.niffer_android.databinding.FragmentAllPeopleBinding
import ru.niffer_android.model.Result
import ru.niffer_android.ui.bottomSheet.SubmitBottomSheet
import ru.niffer_android.ui.bottomSheet.SubmitButtonStyle
import ru.niffer_android.utils.hideLoader
import ru.niffer_android.utils.showError
import ru.niffer_android.utils.showLoader

@AndroidEntryPoint
class AllPeopleFragment : Fragment() {

    private lateinit var binding: FragmentAllPeopleBinding
    private val allPeopleViewModel: PeopleViewModel by activityViewModels()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllPeopleBinding.inflate(
            inflater,
            container,
            false,
        )

        allPeopleViewModel.loadPeople(query = null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val peopleAdapter = PeopleAdapter(object : OnInteractionListener {
            override fun onInviteSendButtonClick(username: String) {
                val modalBottomSheet = SubmitBottomSheet(
                    titleText = getString(R.string.send_invitation),
                    subtitleText = getString(R.string.send_invitation_to, username),
                )
                    .setSubmitButtonText(getString(R.string.send_invitation))
                    .setOnSaveClickListener {
                        allPeopleViewModel.sendInvitation(username)
                    }
                modalBottomSheet.show(parentFragmentManager, SubmitBottomSheet.TAG)
            }

            override fun onAcceptFriendshipButtonClick(username: String) {
                val modalBottomSheet = SubmitBottomSheet(
                    titleText = getString(R.string.accept_invitation),
                    subtitleText = getString(R.string.accept_invitation_from, username),
                )
                    .setSubmitButtonText(getString(R.string.accept_invitation))
                    .setOnSaveClickListener {
                        allPeopleViewModel.acceptInvitation(username)
                    }
                modalBottomSheet.show(parentFragmentManager, SubmitBottomSheet.TAG)
            }

            override fun onDeclineFriendshipButtonClick(username: String) {
                val modalBottomSheet = SubmitBottomSheet(
                    titleText = getString(R.string.decline_invitation),
                    subtitleText = getString(R.string.decline_invitation_from, username),
                )
                    .setSubmitButtonText(getString(R.string.decline_invitation))
                    .setOnSaveClickListener {
                        allPeopleViewModel.declineInvitation(username)
                    }
                    .setSubmitButtonStyle(SubmitButtonStyle.WARNING)
                modalBottomSheet.show(parentFragmentManager, SubmitBottomSheet.TAG)
            }

            override fun onUnfriendButtonClick(username: String) {
                val modalBottomSheet = SubmitBottomSheet(
                    titleText = getString(R.string.unfriend),
                    subtitleText = getString(R.string.unfriend_with, username),
                )
                    .setSubmitButtonText(getString(R.string.unfriend))
                    .setOnSaveClickListener {
                        allPeopleViewModel.deleteFriend(username)
                    }
                    .setSubmitButtonStyle(SubmitButtonStyle.WARNING)
                modalBottomSheet.show(parentFragmentManager, SubmitBottomSheet.TAG)
            }
        })

        binding.allPeopleList.adapter = peopleAdapter

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    val query = s?.toString().orEmpty()
                    allPeopleViewModel.updatePeopleSearchQuery(query)
                    allPeopleViewModel.loadPeople(query)
                    if (query.isNotEmpty()) {
                        binding.searchBarLayout.endIconMode = END_ICON_CLEAR_TEXT
                        binding.searchBarLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cross)
                    } else {
                        binding.searchBarLayout.endIconMode = END_ICON_CUSTOM
                        binding.searchBarLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        lifecycleScope.launch {
            allPeopleViewModel.people.collect { peopleData ->
                when (peopleData) {
                    is Result.Loading -> showLoader()
                    is Result.Success -> {
                        hideLoader()
                        peopleAdapter.submitList(peopleData.data.content)
                    }

                    is Result.Error -> {
                        hideLoader()
                        showError("People data is not loaded")
                    }
                }
            }
        }

        binding.allPeopleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible + 3 >= totalItemCount) {
                    allPeopleViewModel.loadNextPeoplePage()
                }
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            allPeopleViewModel.refreshPeople()
            binding.searchBar.setText(allPeopleViewModel.peopleSearchQuery.value)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}