package com.example.booking.profile.ui

import android.app.AlertDialog
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
    private val adapter: BookingHistoryAdapter = BookingHistoryAdapter(
        cancelBooking = { viewModel.cancelBooking(it) },
        deleteBooking = { viewModel.deleteBooking(it) },
        onItemClick = { navToEstablishmentScreen(it) }
    )
    private var searchJob: Job? = null

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
                getBookings()
                launch { viewModel.userDetails.collect(::onUserDetailsChanged) }
                launch { viewModel.isServiceAvailable.collect(::serviceAvailabilityChanged) }
                launch { viewModel.deleteBookingFlow.collect(::onDeleteBooking) }
                launch { viewModel.bookingCanceledFlow.collect(::onCancelBooking) }
            }
        }
    }

    private fun getBookings() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getBookingHistory().collect { adapter.submitData(it) }
        }
    }

    private fun onDeleteBooking(result: Result<Unit>) {
        if (result.isSuccess) {
            showBookingDeletedMessage()
            getBookings()
        } else {
            showDeleteBookingFailureMessage()
        }
    }

    private fun onCancelBooking(result: Result<Unit>) {
        if (result.isSuccess) {
            showBookingCanceledMessage()
            getBookings()
        } else {
            showCancelBookingFailureMessage()
        }
    }

    private fun onUserDetailsChanged(userDetails: UserDetails) {
        with(binding) {
            textViewName.text = "${userDetails.lastname} ${userDetails.firstname}"
        }
    }

    private fun showBookingDeletedMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.delete_booking_success)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showDeleteBookingFailureMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.delete_booking_failure)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showBookingCanceledMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.cancel_booking_success)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showCancelBookingFailureMessage() {
        with(AlertDialog.Builder(requireContext())) {
            setMessage(R.string.cancel_booking_failure)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun navToSettingsScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_profileFragment_to_settingsFragment)
    }

    private fun navToEstablishmentScreen(establishmentId: Long) {
        val action = ProfileFragmentDirections.actionProfileFragmentToEstablishmentFragment(establishmentId)
        (requireActivity() as MainActivity).navController
            .navigate(action)
    }

    private fun serviceAvailabilityChanged(available: Boolean) {
        with(binding) {
            includeServiceUnavailable.serviceUnavailableLayout.isVisible = !available
            layoutContainer.isVisible = available
        }
    }
}