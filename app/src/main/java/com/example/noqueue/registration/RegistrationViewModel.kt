package com.example.noqueue.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.common.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor( private val authRepository: AuthRepository): ViewModel() {


    private val _currentUser = authRepository.currentLoggedInUser
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    private val _isRegistrationSuccessful = authRepository.isRegistrationSuccessful
    val isRegistrationSuccessful: LiveData<Boolean>
        get() = _isRegistrationSuccessful

    fun doneNavigatingAfterRegistration () {
        authRepository.doneNavigatingAfterRegistration()
    }

    val registrationFailedMessage = authRepository.registrationFailedMessage

    fun register(email: String, password: String, name: String) {
        authRepository.register(email, password, name)
    }
}