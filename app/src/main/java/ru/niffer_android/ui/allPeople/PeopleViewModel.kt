package ru.niffer_android.ui.allPeople

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niffer_android.model.Invitation
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Result
import ru.niffer_android.model.User
import ru.niffer_android.repository.friend.FriendRepository
import ru.niffer_android.repository.invitation.InvitationRepository
import ru.niffer_android.repository.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val invitationRepository: InvitationRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {

    private val _people =
        MutableStateFlow<Result<PagedModel<User>>>(Result.Loading)
    val people: StateFlow<Result<PagedModel<User>>> = _people.asStateFlow()

    private val _friends =
        MutableStateFlow<Result<PagedModel<User>>>(Result.Loading)
    val friends: StateFlow<Result<PagedModel<User>>> = _friends.asStateFlow()

    private var currentPeoplePage = 0
    private var isLoadingMore = false
    private val _peopleSearchQuery = MutableStateFlow("")
    val peopleSearchQuery: StateFlow<String> = _peopleSearchQuery.asStateFlow()
    private val _friendsSearchQuery = MutableStateFlow("")
    val friendsSearchQuery: StateFlow<String> = _friendsSearchQuery.asStateFlow()


    fun loadPeople(query: String?) {
        currentPeoplePage = 0
        viewModelScope.launch {
            userRepository.getAllPeople(page = 0, query = query)
                .collect { peopleData ->
                    _people.value = peopleData
                }
        }
    }

    fun loadFriends(query: String?) {
        viewModelScope.launch {
            friendRepository.getAllFriends(page = 0, query = query)
                .collect { peopleData ->
                    _friends.value = peopleData
                }
        }
    }

    fun loadNextPeoplePage() {
        if (isLoadingMore) return
        isLoadingMore = true
        viewModelScope.launch {
            val people = _people.value
            if (people is Result.Success && currentPeoplePage == people.data.page.totalPages - 1) {
                isLoadingMore = false
                return@launch
            }
            currentPeoplePage++
            userRepository.getAllPeople(page = currentPeoplePage, query = _peopleSearchQuery.value)
                .collect { result ->
                    if (result is Result.Success) {
                        _people.update { current ->
                            when (current) {
                                is Result.Success -> {
                                    isLoadingMore = false
                                    Result.Success(
                                        PagedModel(
                                            content = current.data.content + result.data.content,
                                            page = result.data.page
                                        )
                                    )
                                }

                                else -> {
                                    isLoadingMore = false
                                    result
                                }
                            }
                        }
                    }
                }
        }
    }

    fun refreshPeople() {
        currentPeoplePage = 0
        _peopleSearchQuery.value = ""
        loadPeople("")
    }

    fun loadNextFriendsPage() {
        viewModelScope.launch {
            val friends = _friends.value
            if (friends is Result.Success && currentPeoplePage == friends.data.page.totalPages - 1) return@launch
            currentPeoplePage++
            friendRepository.getAllFriends(page = currentPeoplePage, query = _peopleSearchQuery.value)
                .collect { result ->
                    if (result is Result.Success) {
                        _friends.update { current ->
                            when (current) {
                                is Result.Success -> {
                                    Result.Success(
                                        PagedModel(
                                            content = current.data.content + result.data.content,
                                            page = result.data.page
                                        )
                                    )
                                }

                                else -> result
                            }
                        }
                    }
                }
        }
    }

    fun updatePeopleSearchQuery(query: String) {
        _peopleSearchQuery.value = query
    }

    fun updateFriendsSearchQuery(query: String) {
        _friendsSearchQuery.value = query
        loadFriends(query = query)
    }

    fun sendInvitation(username: String) {
        val invitation = Invitation(username)
        viewModelScope.launch {
            invitationRepository.sendInvitation(invitation)
                .collect { result ->
                    if (result is Result.Success) {
                        val updatedPerson = result.data
                        updatePersonInPeople(updatedPerson)
                        updatePersonInFriends(updatedPerson)
                    }
                }
        }
    }

    fun acceptInvitation(username: String) {
        val invitation = Invitation(username)
        viewModelScope.launch {
            invitationRepository.acceptInvitation(invitation)
                .collect { result ->
                    if (result is Result.Success) {
                        val updatedPerson = result.data
                        updatePersonInPeople(updatedPerson)
                        updatePersonInFriends(updatedPerson)
                    }
                }
        }
    }

    fun declineInvitation(username: String) {
        val invitation = Invitation(username)
        viewModelScope.launch {
            invitationRepository.declineInvitation(invitation)
                .collect { result ->
                    if (result is Result.Success) {
                        val updatedPerson = result.data
                        updatePersonInPeople(updatedPerson)
                        updatePersonInFriends(updatedPerson)
                    }
                }
        }
    }

    fun deleteFriend(username: String) {
        viewModelScope.launch {
            friendRepository.removeFriend(username)
                .collect { result ->
                    if (result is Result.Success) {
                        _people.update { currentState ->
                            when (currentState) {
                                is Result.Success -> {
                                    val updatedPeople = currentState.data.content.map { person ->
                                        if (person.username == username) person.copy(
                                            friendshipStatus = null
                                        ) else person
                                    }
                                    Result.Success(currentState.data.copy(content = updatedPeople))
                                }

                                else -> currentState
                            }
                        }
                        _friends.update { currentState ->
                            when (currentState) {
                                is Result.Success -> {
                                    val updatedPeople = currentState.data.content.map { person ->
                                        if (person.username == username) person.copy(
                                            friendshipStatus = null
                                        ) else person
                                    }
                                    Result.Success(currentState.data.copy(content = updatedPeople))
                                }

                                else -> currentState
                            }
                        }
                    }
                }
        }
    }

    private fun updatePersonInPeople(updatedPerson: User) {
        _people.update { currentState ->
            when (currentState) {
                is Result.Success -> {
                    val updatedPeople = currentState.data.content.map { person ->
                        if (person.id == updatedPerson.id) updatedPerson else person
                    }
                    Result.Success(currentState.data.copy(content = updatedPeople))
                }

                else -> currentState
            }

        }
    }

    private fun updatePersonInFriends(updatedPerson: User) {
        _friends.update { currentState ->
            when (currentState) {
                is Result.Success -> {
                    val updatedPeople = currentState.data.content.map { person ->
                        if (person.id == updatedPerson.id) updatedPerson else person
                    }
                    Result.Success(currentState.data.copy(content = updatedPeople))
                }

                else -> currentState
            }

        }
    }
}