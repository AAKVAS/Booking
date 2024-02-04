package com.example.booking.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.common.ui.showBirthdayPicker
import com.example.booking.common.utils.TextInputDialog
import com.example.booking.common.utils.toStringDate
import com.example.booking.databinding.FragmentServiceBinding
import com.example.booking.databinding.FragmentSettingsBinding
import com.example.booking.profile.ui.viewModel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnLogout.setOnClickListener {
                logout()
            }
            btnChangePassword.setOnClickListener {
                changePassword()
            }
            ivEditLastname.setOnClickListener {
                val context = requireContext()
                TextInputDialog(context, resources.getString(R.string.input_new_lastname)) { newLastname ->
                    val details = viewModel.userDetails.value.copy(lastname = newLastname)
                    viewModel.saveUserDetails(details)
                }.show()
            }
            ivEditFirstname.setOnClickListener {
                TextInputDialog(requireContext(),  resources.getString(R.string.input_new_name)) { newFirstname ->
                    val details = viewModel.userDetails.value.copy(firstname = newFirstname)
                    viewModel.saveUserDetails(details)
                }.show()
            }
            ivEditBirthday.setOnClickListener {
                showBirthdayPicker(
                    tag = SETTING_BIRTHDAY_TAG,
                    date = viewModel.userDetails.value.birthday
                ) { newBirthday ->
                    val details = viewModel.userDetails.value.copy(birthday = newBirthday)
                    viewModel.saveUserDetails(details)
                }
            }
        }
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userDetails.collect(::onUserDetailsChanged)
            }
        }
    }

    private fun onUserDetailsChanged(userDetails: UserDetails) {
        with(binding) {
            tvLastname.text = userDetails.lastname
            tvFirstname.text = userDetails.firstname
            tvBirthday.text = userDetails.birthday.toStringDate()
        }
    }

    private fun logout() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun changePassword() {

    }

    companion object {
        private const val SETTING_BIRTHDAY_TAG = "SETTING_BIRTHDAY_TAG"
    }
}