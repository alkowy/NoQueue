package com.example.noqueue.common

import android.util.Log
import com.example.noqueue.cart.domain.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DataBaseRepository {

     val firebaseDatabase = FirebaseFirestore.getInstance()

    suspend fun getProductByName(product: String, shopName: String): Product {
        return withContext(Dispatchers.IO) {

            val docRef =
                firebaseDatabase.collection("shops/$shopName/products").document(product)
            val productName = "dbName"
            val productPrice = 0.0
            val productImg = ""
            val productFromDb = Product(productName, productImg, productPrice)

            val document = docRef.get().await()
            document?.let {
                productFromDb.name = document.getString("name").toString()
                productFromDb.price = document.getDouble("price") ?: 0.0
                productFromDb.totalPrice = productFromDb.price * productFromDb.quantity
                productFromDb.imgUrl = document.getString("imgUrl").toString()
            }
            productFromDb
        }
    }
    suspend fun getCurrentUserName(uId: String) : String {
        return withContext(Dispatchers.IO) {
            val docRef =
                firebaseDatabase.collection("users").document(uId)
            var name = "placeholder"
            val document = docRef.get().await()
            document.let {
                name = document.getString("name").toString()
                Log.d("dbRepo", "w lecie" +name + uId)
            }
            Log.d("dbRepo", name + uId)
            name
        }
    }
}
