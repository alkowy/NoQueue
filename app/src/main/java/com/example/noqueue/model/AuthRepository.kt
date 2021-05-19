package com.example.noqueue.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    private val fAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: MutableLiveData<FirebaseUser>
        get() = _currentUser

    private val _isRegistrationSuccessful = MutableLiveData<Boolean>()
    val isRegistrationSuccessful: MutableLiveData<Boolean>
        get() = _isRegistrationSuccessful

    private val _registrationFailedMessage = MutableLiveData<String>()
    val registrationFailedMessage: MutableLiveData<String>
        get() = _registrationFailedMessage

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: MutableLiveData<Boolean>
        get() = _isLoginSuccessful

    private val _loginFailedMessage = MutableLiveData<String>()
    val loginFailedMessage: MutableLiveData<String>
        get() = _loginFailedMessage


    fun register(email: String, password: String) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                _currentUser.value = fAuth.currentUser
                _isRegistrationSuccessful.value = true
            }.addOnFailureListener {
                _isRegistrationSuccessful.value = false
                _registrationFailedMessage.value = it.message.toString()
            }
    }

    fun login(email: String, password: String) {
        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                _currentUser.value = fAuth.currentUser
                _isLoginSuccessful.value = true
            }.addOnFailureListener {
                _isLoginSuccessful.value = false
            _loginFailedMessage.value = it.message.toString()
        }
    }
}