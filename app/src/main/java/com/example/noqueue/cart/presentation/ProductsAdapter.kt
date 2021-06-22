package com.example.noqueue.cart.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.databinding.CartProductLayoutBinding

class ProductsAdapter(private var productsList: ArrayList<Product>,
                      private val cartViewModel: CartViewModel) :
    ListAdapter<Product, ProductsAdapter.ViewHolder>(ProductDiffCallback()) {

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val productBinding: CartProductLayoutBinding) :
        RecyclerView.ViewHolder(productBinding.root) {
        fun bind(product: Product) {
            productBinding.productName.text = product.name
            productBinding.productPrice.text = "Price: ${String.format("%.2f", product.totalPrice)}"
            productBinding.productQuantity.text ="Quantity: ${product.quantity}"
            Glide.with(productBinding.root).load(product.imgUrl).placeholder(R.drawable.placeholder)
                .centerCrop().into(productBinding.productImage)

            productBinding.minusProduct.visibility = if (product.quantity <=1) View.GONE else View.VISIBLE

            productBinding.deleteProductImg.setOnClickListener {
                productsList.remove(product)
                cartViewModel.updateTotalValue(productsList)
                notifyItemRemoved(layoutPosition)
            }

            productBinding.plusProduct.setOnClickListener {
                product.quantity++
                product.totalPrice = product.price * product.quantity
                cartViewModel.updateTotalValue(productsList)
                notifyItemChanged(layoutPosition)
            }

            productBinding.minusProduct.setOnClickListener {
                product.quantity--
                product.totalPrice = product.price * product.quantity
                cartViewModel.updateTotalValue(productsList)
                notifyItemChanged(layoutPosition)
            }
        }
    }

//    override fun getItemCount(): Int {
//        return productsList.size
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CartProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("productsAdapter","oncreateviewholder called")
        productsList = cartViewModel.productList.value!!
        submitList(productsList)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //  val product = productsList[position]
        val product = getItem(position)
        holder.bind(product)
    }
}

