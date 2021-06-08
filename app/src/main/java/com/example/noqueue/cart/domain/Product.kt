package com.example.noqueue.cart.domain

data class Product (var name: String, var imgUrl:String, var price: Double = 0.0, var quantity: Int = 1, var totalPrice :Double = price)