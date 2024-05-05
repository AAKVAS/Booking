package com.example.booking.establishments.ui

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
import com.example.booking.bookings.ui.model.BookingEstablishmentParam
import com.example.booking.common.data.LoadingState
import com.example.booking.databinding.FragmentEstablishmentBinding
import com.example.booking.establishments.domain.model.Establishment
import com.example.booking.establishments.domain.model.Hall
import com.example.booking.establishments.ui.adapters.HallsRecyclerAdapter
import com.example.booking.establishments.ui.viewmodel.EstablishmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Фрагмент экрана заведения
 */
@AndroidEntryPoint
class EstablishmentFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: EstablishmentViewModel.EstablishmentViewModelFactory

    private lateinit var binding: FragmentEstablishmentBinding
    private val hallsAdapter: HallsRecyclerAdapter = HallsRecyclerAdapter {
        navToBookingScreen(it)
    }

    private val args: EstablishmentFragmentArgs by navArgs()
    private val viewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModel.provideFactory(viewModelFactory, args.establishmentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstablishmentBinding.inflate(inflater)
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
                viewModel.establishmentFlow.collect(::onEstablishmentChanged)
            }
        }
    }

    private fun onEstablishmentChanged(establishmentState: LoadingState<Establishment>) {
        when(establishmentState) {
            is LoadingState.Loading -> {
                showLoadingSpinner()
            }
            is LoadingState.Failure -> {
                showFailureScreen()
            }
            is LoadingState.Success -> {
                with(binding) {
                    textViewEstablishmentTitle.text = establishmentState.body.title
                    textViewEstablishmentDescription.text = establishmentState.body.description
                    textViewAddress.text = establishmentState.body.address
                    backgroundImg.load(establishmentState.body.imageLink) {
                        crossfade(true)
                        placeholder(R.drawable.loading_img)
                    }
                    hallsAdapter.submitList(establishmentState.body.halls.toMutableList())
                    if (establishmentState.body.favorite) {
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
        val establishment = (viewModel.establishmentFlow.value as LoadingState.Success).body
        val bookingEstablishmentParam = getBookingEstablishmentParam(establishment, hall)
        val action = EstablishmentFragmentDirections.actionEstablishmentFragmentToBookingFragment(bookingEstablishmentParam)
        (requireActivity() as MainActivity).navController
            .navigate(action)
    }

    private fun getBookingEstablishmentParam(establishment: Establishment, hall: Hall): BookingEstablishmentParam {
        return BookingEstablishmentParam(
            uid = establishment.uid,
            id = establishment.id,
            title = establishment.title,
            description = establishment.description,
            address = establishment.address,
            favorite = establishment.favorite,
            hall = hall,
            imageLink = establishment.imageLink
        )
    }
}