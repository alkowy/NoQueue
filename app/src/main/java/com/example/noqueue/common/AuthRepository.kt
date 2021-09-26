package com.example.noqueue.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val fAuth: FirebaseAuth,
                                         private val db: DataBaseRepository) {


    private val _currentLoggedInUser = MutableLiveData<FirebaseUser>(fAuth.currentUser)
    val currentLoggedInUser: LiveData<FirebaseUser>
        get() = _currentLoggedInUser

    private val _isRegistrationSuccessful = MutableLiveData<Boolean>()
    val isRegistrationSuccessful: LiveData<Boolean>
        get() = _isRegistrationSuccessful

    private val _registrationFailedMessage = MutableLiveData<String>()
    val registrationFailedMessage: LiveData<String>
        get() = _registrationFailedMessage

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean>
        get() = _isLoginSuccessful

    private val _loginFailedMessage = MutableLiveData<String>()
    val loginFailedMessage: LiveData<String>
        get() = _loginFailedMessage

    private val _isPasswordChangeSuccessful = MutableLiveData<Boolean>()
    val isPasswordChangeSuccessful: LiveData<Boolean>
        get() = _isPasswordChangeSuccessful

    private val _passwordChangeFailedMessage = MutableLiveData<String>()
    val passwordChangeFailedMessage: LiveData<String>
        get() = _passwordChangeFailedMessage

    fun doneNavigatingAfterRegistration(){
        _isRegistrationSuccessful.value = false
    }

    fun register(email: String, password: String, name: String) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _currentLoggedInUser.value = fAuth.currentUser
            postUserToDB(User(name, _currentLoggedInUser.value!!.uid))
            _isRegistrationSuccessful.value = true
        }.addOnFailureListener {
            _isRegistrationSuccessful.value = false
            _registrationFailedMessage.value = it.message.toString()
        }
    }

    fun login(email: String, password: String) {
        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _currentLoggedInUser.value = fAuth.currentUser
            _isLoginSuccessful.value = true
        }.addOnFailureListener {
            _isLoginSuccessful.value = false
            _loginFailedMessage.value = it.message.toString()
        }
    }

    private fun postUserToDB(user: User) {
        val data = hashMapOf("name" to user.name, "id" to user.uId)
        db.firebaseDatabase.collection("users").document(_currentLoggedInUser.value?.uid.toString())
            .set(data, SetOptions.merge())
    }

    fun logoutCurrentUser() {
        fAuth.signOut()
        _isLoginSuccessful.value = false
    }

    fun sendPasswordEmailReset() {
       fAuth.sendPasswordResetEmail(_currentLoggedInUser.value?.email.toString())
    }
}