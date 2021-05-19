package com.example.noqueue.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.model.AuthRepository
import com.google.firebase.auth.FirebaseUser


class RegistrationViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    private val _currentUser = authRepository.currentUser
    val currentUser : MutableLiveData<FirebaseUser>
        get() = _currentUser

    private val _isRegistrationSuccessful = authRepository.isRegistrationSuccessful
    val isRegistrationSuccessful : MutableLiveData<Boolean>
        get() = _isRegistrationSuccessful

    val registrationFailedMessage = authRepository.registrationFailedMessage

    fun register(email:String, password: String){
        authRepository.register(email,password)
    }



}