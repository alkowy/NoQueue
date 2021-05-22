package com.example.noqueue.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noqueue.model.AuthRepository
import com.google.firebase.auth.FirebaseUser

class ShopsViewModel : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    private val _currentUser = authRepository.currentUser
    val currentUser : MutableLiveData<FirebaseUser>
        get() = _currentUser

    // TODO: add LiveData responsible for accessing correct db (correct shop) 

}