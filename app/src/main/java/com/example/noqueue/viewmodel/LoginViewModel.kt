package com.example.noqueue.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.model.AuthRepository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel(){
    private val authRepository: AuthRepository = AuthRepository()

    private val _currentUser = authRepository.currentUser
    val currentUser : MutableLiveData<FirebaseUser>
        get() = _currentUser

    val isLoginSuccessful = authRepository.isLoginSuccessful

    val failedLoginMessage = authRepository.loginFailedMessage

    fun login (email:String, password :String){
        authRepository.login(email, password)
    }
}