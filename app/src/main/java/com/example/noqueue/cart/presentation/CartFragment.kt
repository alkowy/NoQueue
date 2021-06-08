package com.example.noqueue.cart.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.DataBaseRepository
import com.example.noqueue.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var binding : FragmentCartBinding
    private lateinit  var shopName : String

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentCartBinding.inflate(layoutInflater)

        shopName = arguments?.getString("shopName").toString()
        println("shopname = $shopName")

        val rvProducts = binding.cartProducts

        var productsList = cartViewModel.productList.value

        val productsAdapter = ProductsAdapter(productsList!!, cartViewModel)

        rvProducts.adapter = productsAdapter
        rvProducts.layoutManager = LinearLayoutManager(context)

        val productsDivider: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rvProducts.addItemDecoration(productsDivider)

        binding.scanQRCodeImg.setOnClickListener {
            productsList!!.add(Product("Cola",
                "https://firebasestorage.googleapis.com/v0/b/noqueue-97a7c.appspot.com/o/products%2Fcola.jpg?alt=media&token=6cbf22f8-7d62-490b-a77b-ffa5308dadf1",
                2.99))
            productsAdapter.notifyDataSetChanged()
        }
        binding.scanQRText.setOnClickListener {
            cartViewModel.addProductFromDb("lays", shopName)
            productsAdapter.notifyDataSetChanged()
        }
        cartViewModel.productList.observe(viewLifecycleOwner, Observer {
            productsList = it
            rvProducts.post { productsAdapter.notifyDataSetChanged() }
        })

        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            binding.total.text = String.format("%.2f", it)
        })

        return binding.root
    }
}
