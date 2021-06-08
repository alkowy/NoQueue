package com.example.noqueue.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.example.noqueue.common.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions


class RegistrationViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()
    private val db = DataBaseRepository()

    private val _currentUser = authRepository.currentUser
    val currentUser: MutableLiveData<FirebaseUser>
        get() = _currentUser

    private val _isRegistrationSuccessful = authRepository.isRegistrationSuccessful
    val isRegistrationSuccessful: MutableLiveData<Boolean>
        get() = _isRegistrationSuccessful

    val registrationFailedMessage = authRepository.registrationFailedMessage

    fun register(email: String, password: String, name: String) {
        authRepository.register(email, password, name)
    }
}