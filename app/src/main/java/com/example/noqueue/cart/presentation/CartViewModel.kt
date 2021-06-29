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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val dbRepo: DataBaseRepository,
                                        private val authRepository: AuthRepository): ViewModel() {


    private val _currentUser = authRepository.currentLoggedInUser
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


    suspend fun addProductFromDb(name: String, collectionPath: String) {
        val product = dbRepo.getProductByName(name, collectionPath)
        val actualList = _productsList.value
        actualList!!.add(product)
        updateProductsList(actualList)
        Log.d("cartviewmodel", "product ${product.name}")
        _latestProduct.value = product
    }

    fun addCola(name: String, collectionPath: String) {
        viewModelScope.launch {
            addProductFromDb(name, collectionPath)
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