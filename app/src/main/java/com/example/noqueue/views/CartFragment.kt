package com.example.noqueue.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noqueue.Product
import com.example.noqueue.ProductsAdapter
import com.example.noqueue.R
import com.example.noqueue.databinding.FragmentCartBinding


class CartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentCartBinding.inflate(inflater, container, false)

        val rvProducts = binding.cartProducts

        val productsList = ArrayList<Product>()
        productsList.add(Product("Product1",1,3.0))
        productsList.add(Product("Product2",1,5.0))
        productsList.add(Product("Product3",1,4.0))
        productsList.add(Product("Product4",1,6.99))

        val adapter = ProductsAdapter(productsList)

        rvProducts.adapter = adapter
        rvProducts.layoutManager = LinearLayoutManager(context)

        return binding.root
    }
}
