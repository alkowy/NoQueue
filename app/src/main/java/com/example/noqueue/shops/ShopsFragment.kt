package com.example.noqueue.shops

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noqueue.R
import com.example.noqueue.cart.presentation.ProductsAdapter
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.example.noqueue.common.setTextAnimation
import com.example.noqueue.databinding.FragmentShopsBinding
import com.example.noqueue.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_shops.*


class ShopsFragment : Fragment() {
    private lateinit var binding: FragmentShopsBinding
    private val shopsViewModel: ShopsViewModel by viewModels()
    private lateinit var shopsList: ArrayList<Shop>
    private lateinit var shopsAdapter: ShopAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentShopsBinding.inflate(layoutInflater)
        shopsViewModel.getShopsFromDB()

        observeCurrentName()
        shopsViewModel.getUserName()

        observeShopsList()

        val rvShops = binding.shopsList
        shopsList = shopsViewModel.shopsList.value!!
        shopsAdapter = ShopAdapter(shopsList, shopsViewModel)
        rvShops.adapter = shopsAdapter
        rvShops.layoutManager = GridLayoutManager(context,2)
        shopsAdapter.submitList(shopsList)


        return binding.root
    }


    private fun observeCurrentName() {
        shopsViewModel.currentUserName.observe(viewLifecycleOwner, Observer {
            binding.shopsWelcomeText.setTextAnimation("Hello, $it!\nSelect a store to start shopping.",
                150)
        })
    }

    private fun observeShopsList() {
        shopsViewModel.shopsList.observe(viewLifecycleOwner, Observer {
            shopsList = it
            shopsAdapter.submitList(shopsList)
        })
    }
}