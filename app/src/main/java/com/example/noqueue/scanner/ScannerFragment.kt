package com.example.noqueue.scanner

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.budiyev.android.codescanner.*
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.cart.presentation.CartViewModel
import com.example.noqueue.databinding.FragmentScannerBinding
import com.example.noqueue.databinding.ProductDialogBinding
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch


class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner
    private val cartViewModel: CartViewModel by navGraphViewModels(R.id.cart_scanner_nav_graph) { defaultViewModelProviderFactory }
    private lateinit var shopName: String
    private var hasScanned: Boolean = false
    private lateinit var dialogBinding: ProductDialogBinding
    private var latestProduct = Product("latest", "latestImg")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentScannerBinding.inflate(layoutInflater)
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        shopName = arguments?.getString("shopName").toString()
        dialogBinding = ProductDialogBinding.inflate(layoutInflater)

        setupPermissions()
        setupTheScanner()

        return binding.root
    }


    private fun setupTheScanner() {
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE) // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = false // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            cartViewModel.addProductFromDb(it.text, shopName)
            hasScanned = true
            activity?.runOnUiThread {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_scannerFragment_to_cartFragment,
                        bundleOf("hasScanned" to hasScanned, "shopName" to shopName))
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            Log.i("ScannerFragment", it.message.toString())
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ScannerFragment", "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            1111)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1111 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("ScannerFragment", "Permission has been denied by user")
                } else {
                    Log.i("ScannerFragment", "Permission has been granted by user")
                }
            }
        }
    }

}