package com.example.noqueue.cart.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.example.noqueue.databinding.CartProductLayoutBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel : ViewModel() {

    private val dbRepo = DataBaseRepository()
    private val authRepository: AuthRepository = AuthRepository()
    private val db = FirebaseFirestore.getInstance()

    private val _currentUser = authRepository.currentUser
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    private val _productsList = MutableLiveData<ArrayList<Product>>(arrayListOf())
    val productList: LiveData<ArrayList<Product>>
        get() {
            Log.d("cartviewmodel1", this@CartViewModel.toString())
            return _productsList
        }

    private val _dataChangedEvent = MutableLiveData<Unit>()
    val dataChangedEvent: LiveData<Unit>
        get() = _dataChangedEvent


    fun addProductFromDb(name: String, collectionPath: String) {
        viewModelScope.launch {
            val product = dbRepo.getProductByName(name, collectionPath)
            val actualList = _productsList.value
            actualList!!.add(product)
            updateProductsList(actualList)
            Log.d("cartviewmodel","actuallist $actualList")
            Log.d("cartviewmodel2", this@CartViewModel.toString())
        }
    }

    private fun updateProductsList(newProductsList: ArrayList<Product>) {
        _productsList.value = newProductsList
        updateTotalValue(newProductsList)
        Log.d("cartviewmodel", "udpateproductlist")
    }

    fun updateTotalValue(productList: ArrayList<Product>) {
        var value = 0.0
        productList.forEach {
            value += it.totalPrice
        }
        _totalPrice.value = value
    }

}