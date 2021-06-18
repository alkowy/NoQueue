package com.example.noqueue.cart.presentation


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.budiyev.android.codescanner.*
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.setAllOnClickListener
import com.example.noqueue.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.nav_graph) { defaultViewModelProviderFactory }
    private lateinit var binding: FragmentCartBinding
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var productsAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentCartBinding.inflate(layoutInflater)

        shopName = arguments?.getString("shopName").toString()
        hasScanned = arguments?.getBoolean("hasScanned") ?: false

        observeProductsList()

        // productsList = cartViewModel.productList.value!!

        val rvProducts = binding.cartProducts


        productsAdapter = ProductsAdapter(productsList, cartViewModel)
        rvProducts.adapter = productsAdapter
        rvProducts.layoutManager = LinearLayoutManager(context)


        val productsDivider: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rvProducts.addItemDecoration(productsDivider)


        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            binding.total.text = String.format("%.2f", it)
        })

        binding.scanQRGroup.setAllOnClickListener {
            //  cartViewModel.addProductFromDb("lays", shopName)
            //  productsAdapter.notifyDataSetChanged()
            Navigation.findNavController(it)
                .navigate(com.example.noqueue.R.id.action_cartFragment_to_scannerFragment,
                    bundleOf("shopName" to shopName))
            // binding.scannerView.visibility = View.VISIBLE
        }
        Log.d("cartfragment", hasScanned.toString())
        if (hasScanned) {
            Log.d("cartfragment", hasScanned.toString())
            productsAdapter.submitList(productsList)
        }

        productsList.forEach {
            Log.d("cartFragment", it.name)
        }

        binding.button2.setOnClickListener {
            cartViewModel.addProductFromDb("cola", shopName)
            productsAdapter.submitList(productsList)
            productsAdapter.notifyDataSetChanged()

        }



        return binding.root
    }

    private fun observeProductsList() {
        cartViewModel.productList.observe(viewLifecycleOwner, Observer {
            productsList = it
            productsAdapter.submitList(productsList)
            productsAdapter.notifyDataSetChanged()
            Log.d("cartfragment", "productlist $it")

        })
    }

}

