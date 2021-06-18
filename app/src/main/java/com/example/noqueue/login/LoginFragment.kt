package com.example.noqueue.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.noqueue.R
import com.example.noqueue.common.displayLongToast
import com.example.noqueue.common.displayShortToast
import com.example.noqueue.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding =  FragmentLoginBinding.inflate(layoutInflater)
        navController = findNavController()

        onClickLoginBtn()
        onClickRegisterTextView()

        observeLoginStatus()

        binding.button.setOnClickListener {
            loginViewModel.login("test@gmail.com","123456")
        }

        return binding.root
    }

    private fun onClickRegisterTextView() {
        binding.textViewRegister.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun observeLoginStatus() {
        loginViewModel.isLoginSuccessful.observe(viewLifecycleOwner) {
            if (it) {
                navController.navigate(R.id.action_loginFragment_to_shopsFragment)
            } else {
                loginViewModel.failedLoginMessage.observe(viewLifecycleOwner) { msg ->
                   displayLongToast(context, msg.toString())
                }
            }
        }
    }

    private fun onClickLoginBtn() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditTextView.text.toString()
            val password = binding.passwordEditTextView.text.toString()
            when {
                email.isEmpty() -> displayShortToast(context,"E-mail is required")
                password.isEmpty() -> displayShortToast(context,"Password is required")
                else -> loginViewModel.login(email, password)
            }
        }
    }
 }
