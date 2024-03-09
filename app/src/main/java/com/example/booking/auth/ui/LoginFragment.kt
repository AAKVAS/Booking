package com.example.booking.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.ui.viewmodel.LoginViewModel
import com.example.booking.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeToViewModel()
    }

    private fun bind() {
        with(binding) {
            btnLogin.setOnClickListener {
                login(
                    login = teLogin.editText!!.text.toString(),
                    password = tePassword.editText!!.text.toString()
                )
            }
            btnCatalog.setOnClickListener {
                navToCatalogScreen()
            }
            btnRegister.setOnClickListener {
                navToRegisterScreen()
            }
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.loginDetailsState.collect(::onLoginDetailsChanged) }
                launch { viewModel.loggedFlow.collect(::onLoggedEvent) }
            }
        }
    }

    private fun onLoginDetailsChanged(loginDetails: LoginDetails) {
        with(binding) {
            teLogin.editText!!.setText(loginDetails.login)
            tePassword.editText!!.setText(loginDetails.password)
        }
    }

    private fun onLoggedEvent(logged: Boolean) {
        if (logged) {
            navToCatalogAfterLogin()
        } else {
            showWrongDetailsMessage()
        }
    }

    private fun login(login: String, password: String) {
        viewModel.login(LoginDetails(login, password))
    }

    private fun showWrongDetailsMessage() {
        Toast.makeText(requireActivity(), R.string.wrong_details, Toast.LENGTH_LONG).show()
    }

    private fun navToCatalogScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_loginFragment_to_serviceListFragment)
    }

    private fun navToCatalogAfterLogin() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_serviceListFragment_root)
    }

    private fun navToRegisterScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_loginFragment_to_registrationFragment)
    }
}