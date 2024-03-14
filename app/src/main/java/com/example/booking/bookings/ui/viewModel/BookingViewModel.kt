package com.example.booking.bookings.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.ui.model.BookingServiceParam
import com.example.booking.common.data.LoadingState
import com.example.booking.common.utils.getUUID
import com.example.booking.services.domain.CatalogInteractor
import com.example.booking.services.domain.model.Hall
import com.example.booking.services.domain.model.Place
import com.example.booking.services.domain.model.Service
import com.example.booking.services.ui.viewmodel.ServiceViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookingViewModel @AssistedInject constructor(
    @Assisted val bookingService: BookingServiceParam,
    private val interactor: CatalogInteractor
) : ViewModel() {
    @AssistedFactory
    interface BookingViewModelFactory {
        fun create(bookingService: BookingServiceParam): BookingViewModel
    }

    private val _hallStateFlow: MutableStateFlow<BookingServiceParam> = MutableStateFlow(bookingService)
    val hallStateFlow: StateFlow<BookingServiceParam> = _hallStateFlow.asStateFlow()

    private val _bookingStateFlow: MutableStateFlow<Booking> = MutableStateFlow(
        Booking(
            getUUID(),
            bookingService.hall,
            place = Place(getUUID(),
                0,
                listOf(),
                true,
                0
            ),
            startedAt = DEFAULT_STARTED_AT,
            endedAt = DEFAULT_ENDED_AT,
            date = 0
        )
    )
    val bookingStateFlow: StateFlow<Booking> = _bookingStateFlow.asStateFlow()

    fun updateBookingState(booking: Booking) {
        _bookingStateFlow.update { booking }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory:BookingViewModelFactory, bookingService: BookingServiceParam) : ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(bookingService) as T
                }
            }
        }

        private const val DEFAULT_STARTED_AT = 720
        private const val DEFAULT_ENDED_AT = 780
    }
}