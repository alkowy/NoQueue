package com.example.noqueue.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.common.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _currentUser = authRepository.currentLoggedInUser
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    var isLoginSuccessful = authRepository.isLoginSuccessful

    val failedLoginMessage = authRepository.loginFailedMessage

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }


}