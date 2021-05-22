package com.example.noqueue.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noqueue.R
import com.example.noqueue.databinding.FragmentRegistrationBinding
import com.example.noqueue.viewmodel.RegistrationViewModel


class RegistrationFragment : Fragment() {
//    private var _binding: FragmentRegistrationBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        observeIsRegistrationSuccessful(registrationViewModel, binding)

        onClickRegisterButton(binding, registrationViewModel)

        return binding.root
    }

    private fun onClickRegisterButton(binding: FragmentRegistrationBinding,
                                      registrationViewModel: RegistrationViewModel) {

        binding.registerBtnImageView.setOnClickListener {

            val name = binding.registrationNameEditTextView.text.toString()
            val email = binding.registrationEmailEditTextView.text.toString()
            val password = binding.registrationPasswordEditTextView.text.toString()

            when {
                name.isEmpty() -> Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT).show()
                email.isEmpty() -> Toast.makeText(context, "E-mail is required", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(context,"Password is required",Toast.LENGTH_SHORT).show()
                else -> {
                    registrationViewModel.register(email, password)
                }
            }
        }
    }

    private fun observeIsRegistrationSuccessful(registrationViewModel: RegistrationViewModel,
                                                binding: FragmentRegistrationBinding) {
        registrationViewModel.isRegistrationSuccessful.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_registrationFragment_to_loginFragment)
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
            } else {
                registrationViewModel.registrationFailedMessage.observe(viewLifecycleOwner) {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}