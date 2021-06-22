package com.example.noqueue.cart.presentation


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.budiyev.android.codescanner.*
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.setAllOnClickListener
import com.example.noqueue.databinding.FragmentCartBinding
import com.example.noqueue.databinding.ProductDialogBinding


class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.nav_graph) { defaultViewModelProviderFactory }
    private lateinit var binding: FragmentCartBinding
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var productsAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()
    private lateinit var dialogBinding: ProductDialogBinding
    private var latestProduct = Product("latest", "latestImg")



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopName = arguments?.getString("shopName").toString()
        Log.d("cartFragment", "shopnamecart $shopName")

        hasScanned = arguments?.getBoolean("hasScanned") ?: false

        observeProductsList()
       // observeShopName()
        observeTotalPrice()
        // productsList = cartViewModel.productList.value!!

        observeLatestProduct()

        val rvProducts = binding.cartProducts


        productsAdapter = ProductsAdapter(productsList, cartViewModel)
        rvProducts.adapter = productsAdapter
        rvProducts.layoutManager = LinearLayoutManager(context)


        val productsDivider: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rvProducts.addItemDecoration(productsDivider)




        binding.scanQRGroup.setAllOnClickListener {
            Navigation.findNavController(it)
                .navigate(com.example.noqueue.R.id.action_cartFragment_to_scannerFragment,
                    bundleOf("shopName" to shopName))
        }

        productsList.forEach {
            Log.d("cartFragment", it.name)
        }

        binding.button2.setOnClickListener {
            cartViewModel.addProductFromDb("cola", shopName)
                   }

    }



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        dialogBinding = ProductDialogBinding.inflate(layoutInflater)

        return binding.root
    }
    private fun showDialog() {

        val dialog: AlertDialog = AlertDialog.Builder(context).create()


        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialogBinding.productNameDialog.text = latestProduct.name
        Glide.with(requireContext()).load(latestProduct.imgUrl).placeholder(R.drawable.placeholder)
            .centerCrop().into(dialogBinding.productImageDialog)
        if (dialogBinding.root.parent != null) {
            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
        }
        dialog.show()
    }

    private fun observeLatestProduct() {
        cartViewModel.latestProduct.observe(viewLifecycleOwner, Observer {
            latestProduct = it
            showDialog()
        })
    }
    private fun observeTotalPrice() {
        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            binding.total.text = String.format("%.2f", it)
        })
    }
    private fun observeProductsList() {
        cartViewModel.productList.observe(viewLifecycleOwner, Observer {
            productsList = it
            productsAdapter.submitList(productsList)
            productsAdapter.notifyDataSetChanged()
            Log.d("cartfragment", "productlist $it")

        })
    }
    private fun observeShopName(){
        cartViewModel.shopName.observe(viewLifecycleOwner, Observer {
            shopName = it
        })
    }

}

