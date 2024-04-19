package com.example.booking.auth.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

/**
 * Фрагмент экрана регистрации
 */
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
            textEditBirthday.editText!!.setOnClickListener {
                showDatePicker(DATE_PICKER_TAG) { date ->
                    registrationDetails = registrationDetails.copy(birthday = date)
                    textEditBirthday.editText!!.setText(date.toStringDate())
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
        bindRegistrationDetails()
        viewModel.setRegistrationDetails(registrationDetails)
    }

    private fun bindRegistrationDetails() {
        with(binding) {
            registrationDetails = registrationDetails.copy(
                login = textEditLogin.editText!!.text.toString(),
                password = textEditPassword.editText!!.text.toString(),
                lastname = textEditLastname.editText!!.text.toString(),
                firstname = textEditFirstname.editText!!.text.toString(),
                email = textEditEmail.editText!!.text.toString(),
                phoneNumber = textEditPhoneNumber.editText!!.text.toString()
            )
        }
    }

    private fun onRegistrationDetailsChanged(registrationDetails: RegistrationDetails) {
        with(binding) {
            textEditLogin.editText!!.setText(registrationDetails.login)
            textEditPassword.editText!!.setText(registrationDetails.password)
            textEditBirthday.editText!!.setText(registrationDetails.birthday.toStringDate())
            textEditFirstname.editText!!.setText(registrationDetails.firstname)
            textEditLastname.editText!!.setText(registrationDetails.lastname)
            textEditEmail.editText!!.setText(registrationDetails.email)
            textEditPhoneNumber.editText!!.setText(registrationDetails.phoneNumber)
        }
        this.registrationDetails = registrationDetails
    }

    private fun onRegisteredEvent(registered: Boolean) {
        if (registered) {
            navToCatalogAfterRegister()
        } else {
            showNotRegisteredMessage()
        }
    }

    private fun register() {
        if (validateRegisterDetails()) {
            viewModel.register(registrationDetails)
        }
    }

    private fun navToLoginScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun navToCatalogAfterRegister() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_registrationFragment_to_catalogFragment_root)
    }

    private fun showNotRegisteredMessage() {
        Toast.makeText(requireActivity(), R.string.not_registered, Toast.LENGTH_LONG).show()
    }

    private fun validateRegisterDetails(): Boolean {
        bindRegistrationDetails()
        val repeatPassword = binding.textEditRepeatPassword.editText!!.text.toString()
        if (registrationDetails.lastname.isEmpty() ||
            registrationDetails.firstname.isEmpty() ||
            registrationDetails.password.isEmpty() ||
            registrationDetails.email.isEmpty() ||
            repeatPassword.isEmpty()) {
            showThereAreEmptyFieldsMessage()
            return false
        }
        if (repeatPassword != registrationDetails.password) {
            showPasswordMismatchMessage()
            return false
        }
        return true
    }

    private fun showThereAreEmptyFieldsMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.there_are_empty_fields)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showPasswordMismatchMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.password_mismatch)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }
}