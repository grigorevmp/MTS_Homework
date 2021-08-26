package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikhailgrigorev.mts_home.ProfileFragment
import com.mikhailgrigorev.mts_home.userData.User
import com.mikhailgrigorev.mts_home.userData.UserRepository
import kotlinx.coroutines.launch

typealias ProfileFragmentViewState = ProfileFragment.ViewState

class UserViewModel : ViewModel() {

    val viewState: LiveData<ProfileFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<ProfileFragmentViewState>()

    val currentUser: LiveData<User> get() = _currentUser
    private val _currentUser = MutableLiveData<User>()

    private val userRepository = UserRepository()

    fun loadUser(id: Long) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(id)
                _currentUser.postValue(user)
                _viewState.postValue(ProfileFragmentViewState(isDownloaded = false))
            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun add(userData: User) {
    }

    fun clear(context: Context) {
    }
}

