package com.example.noqueue.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noqueue.R
import com.example.noqueue.common.User
import com.example.noqueue.common.displayLongToast
import com.example.noqueue.common.displayShortToast
import com.example.noqueue.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

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
                name.isEmpty() -> displayShortToast(context, "Name is required")
                email.isEmpty() -> displayShortToast(context, "E-mail is required")
                password.isEmpty() -> displayShortToast(context, "Password is required")
                else -> {
                    registrationViewModel.register(email, password,name)
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
                displayShortToast(context, "Registration successful")
            } else {
                registrationViewModel.registrationFailedMessage.observe(viewLifecycleOwner) {
                    displayLongToast(context, it.toString())
                }
            }
        }
    }
}