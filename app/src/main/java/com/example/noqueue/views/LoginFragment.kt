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
import com.example.noqueue.R
import com.example.noqueue.databinding.FragmentLoginBinding
import com.example.noqueue.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentLoginBinding.inflate(layoutInflater)
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        onClickLoginBtn(binding, loginViewModel)

        observeLoginStatus(loginViewModel, binding)

        onClickRegisterTextView(binding)

        return binding.root
    }

    private fun onClickRegisterTextView(binding: FragmentLoginBinding) {
        binding.textViewRegister.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun observeLoginStatus(loginViewModel: LoginViewModel, binding: FragmentLoginBinding) {
        loginViewModel.isLoginSuccessful.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginFragment_to_shopsFragment)
            } else {
                loginViewModel.failedLoginMessage.observe(viewLifecycleOwner) {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onClickLoginBtn(binding: FragmentLoginBinding, loginViewModel: LoginViewModel) {

        binding.signInButton.setOnClickListener {
            val email = binding.textViewEmail.text.toString()
            val password = binding.textViewPassword.text.toString()
            when {
                email.isEmpty() -> binding.textViewEmail.error = "E-mail is required"
                password.isEmpty() -> binding.textViewPassword.error = "Password is required"
                else -> loginViewModel.login(email, password)
            }
        }
    }
}
