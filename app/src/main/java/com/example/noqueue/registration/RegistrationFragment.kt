package com.example.noqueue.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noqueue.R
import com.example.noqueue.common.GlobalToast
import com.example.noqueue.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                name.length > 13 -> GlobalToast.showShort(context, "Name is too long")
                name.isEmpty() -> GlobalToast.showShort(context, "Name is required")
                email.isEmpty() -> GlobalToast.showShort(context, "E-mail is required")
                password.isEmpty() -> GlobalToast.showShort(context, "Password is required")
                else -> {
                    registrationViewModel.register(email, password, name)
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
                registrationViewModel.doneNavigatingAfterRegistration()
                GlobalToast.showShort(context, "Registration successful")
            } else {
                registrationViewModel.registrationFailedMessage.observe(viewLifecycleOwner) {
                    GlobalToast.showLong(context, it.toString())
                }
            }
        }
    }
}