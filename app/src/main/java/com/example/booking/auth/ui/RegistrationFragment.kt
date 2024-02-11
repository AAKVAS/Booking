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
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.auth.ui.viewmodel.RegistrationViewModel
import com.example.booking.common.ui.showDatePicker
import com.example.booking.common.utils.toStringDate
import com.example.booking.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    private var registrationDetails: RegistrationDetails = RegistrationDetails()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeToViewModel()
    }

    private fun bind() {
        with(binding) {
            btnRegister.setOnClickListener {
                register()
            }
            btnLogin.setOnClickListener {
                navToLoginScreen()
            }
            teBirthday.editText!!.setOnClickListener {
                showDatePicker(DATE_PICKER_TAG) { date ->
                    registrationDetails = registrationDetails.copy(birthday = date)
                    teBirthday.editText!!.setText(date.toStringDate())
                }
                false
            }
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.registrationDetailsFlow.collect(::onRegistrationDetailsChanged) }
                launch { viewModel.registeredFlow.collect(::onRegisteredEvent) }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        with(binding) {
            registrationDetails = registrationDetails.copy(
                login = teLogin.editText!!.text.toString(),
                password = tePassword.editText!!.text.toString(),
                lastname = teLastname.editText!!.text.toString(),
                firstname = teFirstname.editText!!.text.toString()
            )
        }
        viewModel.setRegistrationDetails(registrationDetails)
    }

    private fun onRegistrationDetailsChanged(registrationDetails: RegistrationDetails) {
        with(binding) {
            teLogin.editText!!.setText(registrationDetails.login)
            tePassword.editText!!.setText(registrationDetails.password)
            teBirthday.editText!!.setText(registrationDetails.birthday.toStringDate())
            teFirstname.editText!!.setText(registrationDetails.firstname)
            teLastname.editText!!.setText(registrationDetails.lastname)
        }
        this.registrationDetails = registrationDetails
    }

    private fun onRegisteredEvent(registered: Boolean) {
        if (registered) {
            navToCatalog()
        } else {
            showNotRegisteredMessage()
        }
    }

    private fun register() {
        viewModel.register(registrationDetails)
    }

    private fun navToLoginScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun navToCatalog() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_registrationFragment_to_serviceListFragment)
    }

    private fun showNotRegisteredMessage() {
        Toast.makeText(requireActivity(), R.string.not_registered, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }
}