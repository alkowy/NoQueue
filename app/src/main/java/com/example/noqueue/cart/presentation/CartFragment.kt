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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.setAllOnClickListener
import com.example.noqueue.databinding.FragmentCartBinding
import com.example.noqueue.databinding.ProductDialogBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.cart_scanner_nav_graph) { defaultViewModelProviderFactory }
    private lateinit var binding: FragmentCartBinding
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var productsAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()
    private lateinit var dialogBinding: ProductDialogBinding
    private var latestProduct = Product("latest", "latestImg")
    private var isDialogShown = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopName = arguments?.getString("shopName").toString()

        hasScanned = arguments?.getBoolean("hasScanned") ?: false

        observeProductsList()
        observeTotalPrice()

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

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_cartFragment_to_shopsFragment)
        }

        binding.button2.setOnClickListener {
            cartViewModel.addCola("cola",shopName)
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        // dialogBinding = ProductDialogBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun showDialog(product: Product) {
        isDialogShown = true
        val dialogBinding = ProductDialogBinding.inflate(layoutInflater)
        val dialog: AlertDialog = AlertDialog.Builder(context).create()

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialogBinding.productNameDialog.text = product.name
        Glide.with(requireContext()).load(product.imgUrl).placeholder(R.drawable.placeholder)
            .centerCrop().into(dialogBinding.productImageDialog)

        dialog.show()
        dialog.setOnCancelListener {
            dialog.dismiss()
            isDialogShown = false
        }
    }

    private fun observeLatestProduct() {
        cartViewModel.latestProduct.observe(viewLifecycleOwner, Observer {
            latestProduct = it
            if(!isDialogShown){

                showDialog(latestProduct)
            }
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


        })
    }
}

