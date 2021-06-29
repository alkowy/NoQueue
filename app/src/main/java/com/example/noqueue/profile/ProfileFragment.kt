package com.example.noqueue.profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.cart.domain.Product
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.displayShortToast
import com.example.noqueue.databinding.ChangePasswordDialogBinding
import com.example.noqueue.databinding.FragmentProfileBinding
import com.example.noqueue.databinding.ProductDialogBinding
import com.google.rpc.context.AttributeContext
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var isDialogShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutText.setOnClickListener {
            if (profileViewModel.logoutCurrentUser()) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_profileFragment_to_loginFragment)

            }
        }
        binding.passwordChangeText.setOnClickListener {
            showDialog()
        }

        profileViewModel.isPasswordChangeSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                displayShortToast(context, "Password changed successfully")

            }
        })
        binding.backBtnProfile.setOnClickListener { requireActivity().onBackPressed() }


    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun showDialog() {
        isDialogShown = true
        val dialogBinding = ChangePasswordDialogBinding.inflate(layoutInflater)
        val dialog: AlertDialog = AlertDialog.Builder(context).create()

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialogBinding.textInDialog.text =
            "An e-mail to reset your password will be sent to\n${profileViewModel.currentUserEmail}"

        dialogBinding.yesText.setOnClickListener {
            profileViewModel.sendPasswordEmailReset()
            dialog.dismiss()
            displayShortToast(context, "E-mail has been sent")
        }
        dialogBinding.noText.setOnClickListener { dialog.dismiss() }

        dialog.show()


    }
}