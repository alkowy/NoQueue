package com.example.noqueue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noqueue.databinding.CartProductLayoutBinding

class ProductsAdapter (private val productList: List<Product>) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = CartProductLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(private val productBinding: CartProductLayoutBinding) : RecyclerView.ViewHolder(productBinding.root) {
        fun bind (product: Product){
            productBinding.productName.text = product.name
            productBinding.productQuantity.text = "Quantity: " + product.quantity.toString()
            productBinding.productPrice.text = "Price: " + product.price.toString()
        }


    }
}