package com.example.booking.services.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.booking.R
import com.example.booking.common.data.LoadingState
import com.example.booking.databinding.FragmentServiceBinding
import com.example.booking.services.domain.model.Service
import com.example.booking.services.ui.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ServiceFragment : Fragment() {

    private lateinit var binding: FragmentServiceBinding

    val args: ServiceFragmentArgs by navArgs()
    private val viewModel: ServiceViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras
                .withCreationCallback<ServiceViewModel.ServiceViewModelFactory> { factory ->
                factory.create(args.serviceId)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.serviceFlow.collect(::onServiceChanged)
            }
        }
    }

    private fun onServiceChanged(serviceState: LoadingState<Service>) {
        when(serviceState) {
            is LoadingState.Loading -> {
                showLoadingSpinner()
            }
            is LoadingState.Failure -> {
                showFailureScreen()
            }
            is LoadingState.Success -> {
                with(binding) {
                    textViewServiceTitle.text = serviceState.body.title
                    textViewServiceDescription.text = serviceState.body.description
                }
            }
        }
    }

    private fun showLoadingSpinner() {

    }

    private fun showFailureScreen() {

    }
}