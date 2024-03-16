package com.example.booking.bookings.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booking.bookings.domain.BookingInteractor
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.ui.model.BookingServiceParam
import com.example.booking.bookings.ui.model.BookingValidationStatus
import com.example.booking.common.utils.getUUID
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookingViewModel @AssistedInject constructor(
    @Assisted val bookingService: BookingServiceParam,
    private val interactor: BookingInteractor
) : ViewModel() {
    @AssistedFactory
    interface BookingViewModelFactory {
        fun create(bookingService: BookingServiceParam): BookingViewModel
    }

    private val _bookingStateFlow: MutableStateFlow<Booking> = MutableStateFlow(
        Booking(
            getUUID(),
            bookingService.hall,
            place = null,
            startedAt = DEFAULT_STARTED_AT,
            endedAt = DEFAULT_ENDED_AT,
            date = 0
        )
    )
    val bookingStateFlow: StateFlow<Booking> = _bookingStateFlow.asStateFlow()

    private val _bookingResultFlow: MutableSharedFlow<Result<Unit>> = MutableSharedFlow(0)
    val bookingResultFlow = _bookingResultFlow.asSharedFlow()

    fun updateBookingState(booking: Booking) {
        _bookingStateFlow.update { booking }
    }

    fun bookPlace() {
        viewModelScope.launch {
            val booking = _bookingStateFlow.value
            requireNotNull(booking.place)
            _bookingResultFlow.emit(interactor.bookPlace(booking))
        }
    }

    fun validateBooking(): BookingValidationStatus {
        val booking = _bookingStateFlow.value
        if (booking.startedAt >= booking.endedAt) {
            return BookingValidationStatus.WRONG_TIME
        }
        if (booking.place == null) {
            return BookingValidationStatus.PLACE_NOT_SELECTED
        }
        return BookingValidationStatus.VALID
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