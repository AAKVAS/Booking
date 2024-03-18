package com.example.booking.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.common.utils.isNetworkAvailable
import com.example.booking.common.utils.showNetworkNotAvailableMessage
import com.example.booking.databinding.FragmentProfileBinding
import com.example.booking.profile.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Фрагмент профиля пользователя приложения
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val adapter: BookingHistoryAdapter = BookingHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeToViewModel()
    }

    override fun onResume() {
        super.onResume()
        val context = requireContext()
        if (!context.isNetworkAvailable()) {
            context.showNetworkNotAvailableMessage()
        } else {
            viewModel.checkServiceAvailable()
        }
    }

    private fun bind() {
        with(binding) {
            imageViewSettings.setOnClickListener {
                navToSettingsScreen()
            }
            recyclerViewHistory.adapter = adapter
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.userDetails.collect(::onUserDetailsChanged) }
                launch { viewModel.getBookingHistory().collect { adapter.submitData(it) } }
                launch { viewModel.isServiceAvailable.collect(::serviceAvailabilityChanged) }
            }
        }
    }

    private fun onUserDetailsChanged(userDetails: UserDetails) {
        with(binding) {
            textViewName.text = "${userDetails.lastname} ${userDetails.firstname}"
        }
    }

    private fun navToSettingsScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_profileFragment_to_settingsFragment)
    }

    private fun serviceAvailabilityChanged(available: Boolean) {
        with(binding) {
            includeServiceUnavailable.serviceUnavailableLayout.isVisible = !available
            layoutContainer.isVisible = available
        }
    }
}