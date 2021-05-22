package com.example.noqueue.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.navigation.Navigation
import com.example.noqueue.R
import com.example.noqueue.databinding.FragmentShopsBinding


class ShopsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShopsBinding.inflate(inflater,container,false)

        shop01OnClickNavigation(binding)
        shop02OnClickNavigation(binding)
        shop03OnClickNavigation(binding)
        shop04OnClickNavigation(binding)



        return binding.root
    }
    /*
    TODO: some data needs to be passed depending on the shop that was chosen (which shop, products available, prices etc.). Now it's just a navigation.
     */
    private fun shop01OnClickNavigation(binding: FragmentShopsBinding) {
        binding.shop01.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_shopsFragment_to_cartFragment)
        }
    }
    private fun shop02OnClickNavigation(binding: FragmentShopsBinding) {
        binding.shop01.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_shopsFragment_to_cartFragment)
        }
    }
    private fun shop03OnClickNavigation(binding: FragmentShopsBinding) {
        binding.shop01.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_shopsFragment_to_cartFragment)
        }
    }
    private fun shop04OnClickNavigation(binding: FragmentShopsBinding) {
        binding.shop01.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_shopsFragment_to_cartFragment)
        }
    }

}