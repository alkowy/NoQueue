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
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.setAllOnClickListener
import com.example.noqueue.databinding.FragmentCartBinding
import com.example.noqueue.databinding.ProductDialogBinding


class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.cart_scanner_nav_graph) { defaultViewModelProviderFactory }
    private var binding: FragmentCartBinding? = null
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var productsAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()

    private var isDialogOpened = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopName = arguments?.getString("shopName").toString()

        hasScanned = arguments?.getBoolean("hasScanned") ?: false

        observeProductsList()
        observeTotalPrice()

        observeLatestProduct()

        val rvProducts = binding!!.cartProducts

        productsAdapter = ProductsAdapter(productsList, cartViewModel)
        rvProducts.adapter = productsAdapter
        rvProducts.layoutManager = LinearLayoutManager(context)

        val productsDivider: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rvProducts.addItemDecoration(productsDivider)

        binding?.let {
            it.scanQRGroup.setAllOnClickListener {
                Navigation.findNavController(it)
                    .navigate(com.example.noqueue.R.id.action_cartFragment_to_scannerFragment,
                        bundleOf("shopName" to shopName))
            }

            it.backBtn.setOnClickListener {
//                Navigation.findNavController(it).navigate(R.id.action_cartFragment_to_shopsFragment)
                requireActivity().onBackPressed()
            }

            it.button2.setOnClickListener {
                cartViewModel.addProductFromDb("cola", shopName)
            }
        }


    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)

        return binding!!.root
    }

    private fun showDialog(latestProduct: Product) {
        isDialogOpened = true
        val dialogBinding = ProductDialogBinding.inflate(layoutInflater)

        dialogBinding.let {
            val dialog: AlertDialog = AlertDialog.Builder(context).create()

            dialog.setView(it.root)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            Log.d("RRRRRRRR","latest w shwodialog $latestProduct")
            it.productNameDialog.text = latestProduct.name
            Glide.with(requireContext()).load(latestProduct.imgUrl)
                .placeholder(R.drawable.placeholder).centerCrop().into(it.productImageDialog)

            dialog.show()
            dialog.setOnCancelListener {
                it.dismiss()
                isDialogOpened = false
            }
        }

    }

    private fun observeLatestProduct() {
        cartViewModel.latestProduct.observe(viewLifecycleOwner, Observer {


            if (cartViewModel.latestProductValue != it) {
                if (!isDialogOpened) {

                    showDialog(it)
                    Log.d("RRRRRRRR", " $it    :     $cartViewModel.latestProductValue")
                }
            }
            cartViewModel.latestProductValue = it
        })
    }

    private fun observeTotalPrice() {
        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            binding!!.total.text = String.format("%.2f", it)
        })
    }

    private fun observeProductsList() {
        cartViewModel.productList.observe(viewLifecycleOwner, Observer {
            productsList = it
            productsAdapter.submitList(productsList)
            productsAdapter.notifyDataSetChanged()


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}

