package com.example.booking.profile.ui

import android.app.AlertDialog
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
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.common.ui.showDatePicker
import com.example.booking.common.utils.TextInputDialog
import com.example.booking.common.utils.toStringDate
import com.example.booking.databinding.DialogChangePasswordBinding
import com.example.booking.databinding.FragmentSettingsBinding
import com.example.booking.profile.domain.model.ChangePasswordResult
import com.example.booking.profile.ui.viewModel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Фрагмент настроек аккаунта пользователя
 */
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
                viewModel.logout()
            }
            btnChangePassword.setOnClickListener {
                changePassword()
            }
            ivEditLastname.setOnClickListener {
                showEditLastnameDialog()
            }
            ivEditFirstname.setOnClickListener {
                showEditFirstnameDialog()
            }
            ivEditBirthday.setOnClickListener {
                showDatePicker(
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

    private fun showEditLastnameDialog() {
        TextInputDialog(requireContext(), resources.getString(R.string.input_new_lastname)) { newLastname ->
            val details = viewModel.userDetails.value.copy(lastname = newLastname)
            viewModel.saveUserDetails(details)
        }.show()
    }

    private fun showEditFirstnameDialog() {
        TextInputDialog(requireContext(),  resources.getString(R.string.input_new_name)) { newFirstname ->
            val details = viewModel.userDetails.value.copy(firstname = newFirstname)
            viewModel.saveUserDetails(details)
        }.show()
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.userDetails.collect(::onUserDetailsChanged) }
                launch { viewModel.detailsSavedResult.collect(::onDetailsSaved) }
                launch { viewModel.logoutResult.collect(::onLogout) }
                launch { viewModel.changePasswordResult.collect(::onChangePassword) }
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

    private fun onDetailsSaved(result: Result<Unit>) {
        if (result.isSuccess) {
            Toast.makeText(requireContext(), R.string.success_saved, Toast.LENGTH_LONG).show()
        } else {
            with(AlertDialog.Builder(requireContext())) {
                setTitle(R.string.error_happen)
                setMessage(R.string.data_not_saved)
                show()
            }
        }
    }

    private fun onLogout(result: Result<Unit>) {
        if (result.isSuccess) {
            (requireActivity() as MainActivity).navController
                .navigate(R.id.action_settingsFragment_to_loginFragment)
        } else {
            showNotSavedAlertDialog()
        }
    }

    private fun onChangePassword(changePasswordResult: ChangePasswordResult) {
        when(changePasswordResult) {
            is ChangePasswordResult.Success -> {
                Toast.makeText(requireContext(), R.string.success_saved, Toast.LENGTH_LONG).show()
            }
            is ChangePasswordResult.WrongOldPassword -> {
                with(AlertDialog.Builder(requireContext())) {
                    setMessage(R.string.wrong_old)
                    show()
                }
            }
            is ChangePasswordResult.Failure -> {
                showNotSavedAlertDialog()
            }
        }
    }

    private fun changePassword() {
        val binding: DialogChangePasswordBinding = DialogChangePasswordBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val oldPassword = binding.teNewPassword.editText!!.text.toString()
                val newPassword = binding.teNewPassword.editText!!.text.toString()
                viewModel.changePassword(oldPassword, newPassword)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()

        alertDialog.show()
    }

    private fun showNotSavedAlertDialog() {
        with(AlertDialog.Builder(requireContext())) {
            setTitle(R.string.error_happen)
            setMessage(R.string.logout_failed)
            show()
        }
    }

    companion object {
        private const val SETTING_BIRTHDAY_TAG = "SETTING_BIRTHDAY_TAG"
    }
}