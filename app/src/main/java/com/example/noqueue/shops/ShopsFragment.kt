package com.example.noqueue.shops

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.noqueue.R
import com.example.noqueue.common.AuthRepository
import com.example.noqueue.common.DataBaseRepository
import com.example.noqueue.common.setTextAnimation
import com.example.noqueue.databinding.FragmentShopsBinding
import com.example.noqueue.login.LoginViewModel


class ShopsFragment : Fragment() {
    private lateinit var binding: FragmentShopsBinding
    private lateinit var bundle: Bundle
    private val shopsViewModel: ShopsViewModel by viewModels()
    private val authRepository = AuthRepository()
    private val db = DataBaseRepository()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentShopsBinding.inflate(layoutInflater)

        observeCurrentName()

        shopsViewModel.getUserName()

        shop01SetOnClickNavigation()
        shop02OnClickNavigation()
        shop03OnClickNavigation()
        shop04OnClickNavigation()

        return binding.root
    }

    private fun shop01SetOnClickNavigation() {
        binding.shop01.setOnClickListener {
            bundle = bundleOf("shopName" to "lidl")
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_shopsFragment_to_cartFragment, bundle)
        }
    }

    private fun shop02OnClickNavigation() {
        binding.shop02.setOnClickListener {
            bundle = bundleOf("shopName" to "biedronka")
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_shopsFragment_to_cartFragment, bundle)
        }
    }

    private fun shop03OnClickNavigation() {
        binding.shop03.setOnClickListener {
            bundle = bundleOf("shopName" to "auchan")
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_shopsFragment_to_cartFragment, bundle)
        }
    }

    private fun shop04OnClickNavigation() {
        binding.shop04.setOnClickListener {
            bundle = bundleOf("shopName" to "carrefour")
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_shopsFragment_to_cartFragment, bundle)
        }
    }

    private fun observeCurrentName() {
        shopsViewModel.currentUserName.observe(viewLifecycleOwner, Observer {
            binding.shopsWelcomeText.setTextAnimation("Hello, $it!\nSelect a store to start shopping.",
                150)
        })
    }
}