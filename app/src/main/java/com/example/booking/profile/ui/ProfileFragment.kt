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
import com.example.booking.databinding.FragmentProfileBinding
import com.example.booking.profile.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ivSettings.setOnClickListener {
                navToSettingsScreen()
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
            tvName.text = "${userDetails.lastname} ${userDetails.firstname}"
        }
    }

    private fun navToSettingsScreen() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_profileFragment_to_settingsFragment)
    }
}