package com.example.noqueue.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noqueue.MainActivity
import com.example.noqueue.R
import com.example.noqueue.databinding.FragmentRegistrationBinding
import com.example.noqueue.viewmodel.RegistrationViewModel


class RegistrationFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //view binding
        val binding = FragmentRegistrationBinding.inflate(layoutInflater)

        // viewmodel
        val registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)


        //observer of isRegistrationSuccessful + navigation/toast
        observeIsRegistrationSuccessful(registrationViewModel, binding)

        // register btn click => viewmodel.register => authrepository.register
        onClickRegisterButton(binding, registrationViewModel)

        return binding.root
    }

    private fun onClickRegisterButton(binding: FragmentRegistrationBinding,
                                      registrationViewModel: RegistrationViewModel) {

        binding.registerButton.setOnClickListener {

            val name = binding.registrationNameTextView.text.toString()
            val email = binding.registrationEmailTextView.text.toString()
            val password = binding.registrationPasswordTextView.text.toString()

            when {
                name.isEmpty() -> {
                    binding.registrationNameTextView.error = "Name is required"
                }
                email.isEmpty() -> {
                    binding.registrationEmailTextView.error = "E-mail is required"
                }
                password.isEmpty() -> {
                    binding.registrationPasswordTextView.error = "Password is required"
                }
                else -> {
                    registrationViewModel.register(email, password)
                }
            }

        }
    }

    private fun observeIsRegistrationSuccessful(registrationViewModel: RegistrationViewModel,
                                                binding: FragmentRegistrationBinding) {
        registrationViewModel.isRegistrationSuccessful.observe(viewLifecycleOwner, Observer {

            if (it == true) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_registrationFragment_to_loginFragment)
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,
                    registrationViewModel.registrationFailedMessage.value.toString(),
                    Toast.LENGTH_LONG).show()
            }
        })
    }


}