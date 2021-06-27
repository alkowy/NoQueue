package com.example.noqueue.cart.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
            return _productsList
        }

    private val _shopName = MutableLiveData<String>()
    val shopName: LiveData<String>
        get() = _shopName

    private val _dataChangedEvent = MutableLiveData<Unit>()
    val dataChangedEvent: LiveData<Unit>
        get() = _dataChangedEvent

    private var _latestProduct = MutableLiveData<Product>()
    val latestProduct: LiveData<Product>
        get() = _latestProduct

     var latestProductValue = Product("latest", "latestImg")




    fun addProductFromDb(name: String, collectionPath: String) {
        viewModelScope.launch {
            val product = dbRepo.getProductByName(name, collectionPath)

            val actualList = _productsList.value
            actualList!!.add(product)
            updateProductsList(actualList)
            _latestProduct.value = product
        }
    }


    private fun updateProductsList(newProductsList: ArrayList<Product>) {
        _productsList.value = newProductsList
        updateTotalValue(newProductsList)
    }

    fun updateTotalValue(productList: ArrayList<Product>) {
        var value = 0.0
        productList.forEach {
            value += it.totalPrice
        }
        _totalPrice.value = value
    }

    fun setShopName(shopName: String) {
        _shopName.value = shopName
    }
}