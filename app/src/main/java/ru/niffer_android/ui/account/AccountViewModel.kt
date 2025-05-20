package ru.niffer_android.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.niffer_android.model.Result
import ru.niffer_android.model.User
import ru.niffer_android.repository.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _user = MutableStateFlow<Result<User>>(Result.Loading)
    val user: StateFlow<Result<User>> = _user.asStateFlow()


    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser()
                .collect { userData ->
                    _user.value = userData
                }
        }
    }

    fun updateUser(newName: String) {
        viewModelScope.launch {
            val user = _user.value
            if (user is Result.Success) {
                userRepository.updateUser(user.data.copy(fullname = newName)).collect { result ->
                    if (result is Result.Success) {
                        _user.value = result
                    }
                }
            }
        }
    }

    fun updateAvatar(base64Image: String) {
        viewModelScope.launch {
            val user = _user.value
            if (user is Result.Success) {
                userRepository.updateUser(user.data.copy(photo = base64Image)).collect { result ->
                    if (result is Result.Success) {
                        _user.value = result
                    }
                }
            }
        }
    }
}