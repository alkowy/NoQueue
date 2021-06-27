package com.example.noqueue.common

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val fAuth: FirebaseAuth,
                                         private val db: DataBaseRepository) {


    private val _currentUser = MutableLiveData<FirebaseUser>(fAuth.currentUser)
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


    fun register(email: String, password: String, name: String) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _currentUser.value = fAuth.currentUser
            postUserToDB(User(name, _currentUser.value!!.uid))
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

    private fun postUserToDB(user: User) {
        val data = hashMapOf("name" to user.name, "id" to user.uId)
        db.firebaseDatabase.collection("users").document(_currentUser.value?.uid.toString())
            .set(data, SetOptions.merge())
    }
}