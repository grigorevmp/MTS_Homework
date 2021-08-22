package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.ProfileFragment
import com.mikhailgrigorev.mts_home.userData.User
import com.mikhailgrigorev.mts_home.userData.UserModel

typealias ProfileFragmentViewState = ProfileFragment.ViewState

class UserViewModel : ViewModel() {

    private val model = UserModel()

    val viewState: LiveData<ProfileFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<ProfileFragmentViewState>()

    val currentUser: LiveData<User> get() = _currentUser
    private val _currentUser = MutableLiveData<User>()

    fun loadUser(id: Long) {
        model.loadUser(object : UserModel.LoadUserByIdCallback {
            override fun onLoad(user: User?) {
                _currentUser.postValue(user)
                _viewState.postValue(ProfileFragmentViewState(isDownloaded = false))
            }
        }, id)
    }

    fun add(userData: User) {
    }

    fun clear(context: Context) {
    }
}
