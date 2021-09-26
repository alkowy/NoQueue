package com.example.noqueue.shops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(private val authRepository: AuthRepository,
                                         private val db: DataBaseRepository): ViewModel() {


    private val _currentUser = authRepository.currentLoggedInUser
    val currentUser: LiveData<FirebaseUser>
    get() = _currentUser

    private val _currentUserName = MutableLiveData<String>()
    val currentUserName: LiveData<String>
    get() = _currentUserName

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
    get() = _userName

    private val _shopsList = MutableLiveData<ArrayList<Shop>>(arrayListOf())
    val shopsList: LiveData<ArrayList<Shop>>
    get() = _shopsList

    fun getShopsFromDB() {
        viewModelScope.launch {
            val shopsList = db.getShopsFromDB()
            _shopsList.value = shopsList
        }
    }

    fun updateShopsList(newShopsList: ArrayList<Shop>) {
        _shopsList.value = newShopsList
    }

    fun getUserName() {
        viewModelScope.launch {
            val name = db.getCurrentUserName(_currentUser.value!!.uid)
            _currentUserName.value = name
        }
    }
}