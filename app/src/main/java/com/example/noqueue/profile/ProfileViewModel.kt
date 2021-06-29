package com.example.noqueue.profile

import androidx.lifecycle.ViewModel
import com.example.noqueue.common.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(var fAuthRepository: AuthRepository): ViewModel() {



    val isPasswordChangeSuccessful = fAuthRepository.isPasswordChangeSuccessful

    val currentUserEmail = fAuthRepository.currentLoggedInUser.value?.email.toString()

    fun logoutCurrentUser(): Boolean{
        if(fAuthRepository.currentLoggedInUser.value !=null){
            fAuthRepository.logoutCurrentUser()
            return true
        }
        return false
    }

    fun sendPasswordEmailReset(){
        fAuthRepository.sendPasswordEmailReset()
    }

}