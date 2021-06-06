package com.example.noqueue.shops

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ShopsViewModel : ViewModel() {


    private val authRepository: AuthRepository = AuthRepository()
    private val db = DataBaseRepository()

    private val _currentUser = authRepository.currentUser
    val currentUser: MutableLiveData<FirebaseUser>
        get() = _currentUser

    private val _currentUserName = MutableLiveData<String>()
    val currentUserName: LiveData<String>
        get() = _currentUserName

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    fun getUserName() {
        viewModelScope.launch {
            val name = db.getCurrentUserName(_currentUser.value!!.uid)
            Log.d("shopsviewmodel", "w launchu  " +name)
            _currentUserName.value = name
        }
    }
}