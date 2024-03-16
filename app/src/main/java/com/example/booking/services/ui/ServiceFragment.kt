package com.example.booking.services.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.bookings.ui.model.BookingServiceParam
import com.example.booking.common.data.LoadingState
import com.example.booking.databinding.FragmentServiceBinding
import com.example.booking.services.domain.model.Hall
import com.example.booking.services.domain.model.Service
import com.example.booking.services.ui.adapters.HallsRecyclerAdapter
import com.example.booking.services.ui.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ServiceFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ServiceViewModel.ServiceViewModelFactory

    private lateinit var binding: FragmentServiceBinding
    private val hallsAdapter: HallsRecyclerAdapter = HallsRecyclerAdapter {
        navToBookingScreen(it)
    }

    val args: ServiceFragmentArgs by navArgs()
    private val viewModel: ServiceViewModel by viewModels {
        ServiceViewModel.provideFactory(viewModelFactory, args.serviceId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeToViewModel()
    }

    private fun bind() {
        with(binding) {
            buttonStar.setOnClickListener {
                viewModel.changeFavoriteStatus()
            }
            recyclerViewHalls.adapter = hallsAdapter
        }
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
                    backgroundImg.load(serviceState.body.imageLink) {
                        crossfade(true)
                        placeholder(R.drawable.loading_img)
                    }
                    hallsAdapter.submitList(serviceState.body.halls.toMutableList())
                    if (serviceState.body.favorite) {
                        buttonStar.setBackgroundResource(R.drawable.filled_star)
                    } else {
                        buttonStar.setBackgroundResource(R.drawable.star)
                    }
                }
            }
        }
    }

    private fun showLoadingSpinner() {

    }

    private fun showFailureScreen() {

    }

    private fun navToBookingScreen(hall: Hall) {
        val service = (viewModel.serviceFlow.value as LoadingState.Success).body
        val bookingServiceParam = getBookingServiceParam(service, hall)
        val action = ServiceFragmentDirections.actionServiceFragmentToBookingFragment(bookingServiceParam)
        (requireActivity() as MainActivity).navController
            .navigate(action)
    }

    private fun getBookingServiceParam(service: Service, hall: Hall): BookingServiceParam {
        return BookingServiceParam(
            uid = service.uid,
            id = service.id,
            title = service.title,
            description = service.description,
            address = service.address,
            favorite = service.favorite,
            hall = hall,
            imageLink = service.imageLink
        )
    }
}