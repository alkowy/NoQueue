package com.example.noqueue.cart.presentation


import android.app.AlertDialog
import android.os.Bundle
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
import com.example.noqueue.common.displayShortToast
import com.example.noqueue.common.setAllOnClickListener
import com.example.noqueue.databinding.ChangePasswordDialogBinding
import com.example.noqueue.databinding.FragmentCartBinding
import com.example.noqueue.databinding.PayDialogBinding
import com.example.noqueue.databinding.ProductDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.cart_scanner_nav_graph) { defaultViewModelProviderFactory }
    private lateinit var binding: FragmentCartBinding
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var productsAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()
    private var latestProduct = Product("latest", "latestImg")
    private var isDialogShown = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopName = arguments?.getString("shopName").toString()

        hasScanned = arguments?.getBoolean("hasScanned") ?: false

        observeProductsList()
        observeTotalPrice()

        observeLatestProduct()

        observeIsProductNull()

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

        binding.backBtn.setOnClickListener { navigateTo(DestinationEnum.SHOP) }
        binding.cartProfile.setOnClickListener { navigateTo(DestinationEnum.PROFILE) }

        binding.button2.setOnClickListener {
            cartViewModel.addCola("cola", shopName)
        }
        binding.cartWallet.setOnClickListener { showDialogPay() }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun navigateTo(destinationEnum: DestinationEnum) {
        val navController = Navigation.findNavController(binding.root)
        when (destinationEnum) {
            DestinationEnum.PROFILE -> navController.navigate(R.id.action_global_profileFragment2)
            DestinationEnum.SHOP -> navController.navigate(R.id.action_cartFragment_to_shopsFragment)
        }
    }

    private fun showDialogScannedProduct(product: Product) {
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
    private fun showDialogPay() {
        isDialogShown = true
        val payDialogBinding : PayDialogBinding = PayDialogBinding.inflate(layoutInflater)
        val payDialog: AlertDialog = AlertDialog.Builder(context).create()

        payDialog.setView(payDialogBinding.root)
        payDialog.setCancelable(true)
        payDialog.setCanceledOnTouchOutside(true)

        payDialogBinding.dialogTotalPrice.text = cartViewModel.totalPrice.value.toString()

        payDialogBinding.dialogPay.setOnClickListener {
           navigateTo(DestinationEnum.SHOP)
            payDialog.dismiss()
            displayShortToast(context, "Thank you for your purchase!")
        }
        payDialogBinding.dialogCancel.setOnClickListener { payDialog.dismiss() }

        payDialog.show()
        payDialog.setOnCancelListener {
            payDialog.dismiss()
            isDialogShown = false
        }


    }

    private fun observeLatestProduct() {
        cartViewModel.latestProduct.observe(viewLifecycleOwner, Observer {
            latestProduct = it
            if (!isDialogShown) {
                showDialogScannedProduct(latestProduct)
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

    private fun observeIsProductNull() {
        cartViewModel.isProductNull.observe(viewLifecycleOwner, Observer {
            if (it) {
                displayShortToast(context, "Could not scan this product")
                cartViewModel.doneShowingToastAfterNullProduct()
            }
        })
    }
}

enum class DestinationEnum { PROFILE, SHOP }

