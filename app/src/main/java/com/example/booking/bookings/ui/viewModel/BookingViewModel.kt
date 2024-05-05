package com.example.booking.bookings.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booking.bookings.domain.BookingInteractor
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.ui.model.BookingEstablishmentParam
import com.example.booking.bookings.ui.model.BookingValidationStatus
import com.example.booking.common.utils.getUUID
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * [ViewModel] экрана бронирования
 */
class BookingViewModel @AssistedInject constructor(
    @Assisted val bookingEstablishment: BookingEstablishmentParam,
    private val interactor: BookingInteractor
) : ViewModel() {

    private val _bookButtonEnabledStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bookButtonEnabledStateFlow = _bookButtonEnabledStateFlow.asStateFlow()

    /**
     * Фабрика для создания класса [BookingViewModel]
     */
    @AssistedFactory
    interface BookingViewModelFactory {
        fun create(bookingService: BookingEstablishmentParam): BookingViewModel
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val bookButtonEnabled = interactor.isUserLogged()
            _bookButtonEnabledStateFlow.emit(bookButtonEnabled)
            updatePlaces()
        }
    }

    private val _bookingStateFlow: MutableStateFlow<Booking> = MutableStateFlow(
        Booking(
            getUUID(),
            bookingEstablishment.hall,
            place = null,
            startedAt = DEFAULT_STARTED_AT,
            endedAt = DEFAULT_ENDED_AT,
            date = Calendar.getInstance().timeInMillis
        )
    )

    /**
     * Состояние бронирования
     */
    val bookingStateFlow: StateFlow<Booking> = _bookingStateFlow.asStateFlow()

    private val _bookingResultFlow: MutableSharedFlow<Result<Unit>> = MutableSharedFlow(0)
    val bookingResultFlow = _bookingResultFlow.asSharedFlow()

    /**
     * Изменить состояние бронирования
     */
    fun updateBookingState(booking: Booking) {
        _bookingStateFlow.update { booking }
    }

    /**
     * Обновить список мест
     */
    fun updatePlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val booking = _bookingStateFlow.value
                val places = interactor.getPlaces(
                    hallId = booking.hall.id,
                    startedAt = booking.startedAt,
                    endedAt = booking.endedAt,
                    date = booking.date
                )
                val hall = booking.hall.copy(places = places)
                _bookingStateFlow.update { booking.copy(hall = hall) }
            }
            catch (ignored: Exception) {
                println(ignored.message)
            }
        }
    }

    /**
     * Забронировать место
     */
    fun bookPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            val booking = _bookingStateFlow.value
            requireNotNull(booking.place)
            _bookingResultFlow.emit(interactor.bookPlace(booking))
        }
    }

    /**
     * Проверить корректность данных для создания бронирования
     */
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
        fun provideFactory(factory:BookingViewModelFactory, bookingService: BookingEstablishmentParam) : ViewModelProvider.Factory {
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