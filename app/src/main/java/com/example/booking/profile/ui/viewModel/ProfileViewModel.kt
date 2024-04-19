package com.example.booking.profile.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.common.ui.viewModel.NetworkViewModel
import com.example.booking.common.utils.getUUID

import com.example.booking.profile.domain.ProfileInteractor
import com.example.booking.profile.domain.model.BookingHistory
import com.example.booking.profile.ui.model.BookingHistoryUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
 * [ViewModel] для фрагмента профиля пользователя приложения
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    override val interactor: ProfileInteractor
) : NetworkViewModel(interactor) {
    private val _userDetails: MutableStateFlow<UserDetails> = MutableStateFlow(UserDetails())

    /**
     * Данные об аккаунте прользователя
     */
    val userDetails: StateFlow<UserDetails>
        get() = _userDetails

    private val _bookingCanceledFlow: MutableSharedFlow<Result<Unit>> = MutableSharedFlow(0)

    /**
     * Результат попытки отмены бронирования
     */
    val bookingCanceledFlow = _bookingCanceledFlow.asSharedFlow()

    private val _deleteBookingFlow: MutableSharedFlow<Result<Unit>> = MutableSharedFlow(0)

    /**
     * Результат попытки удаления бронирования из истории
     */
    val deleteBookingFlow = _deleteBookingFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getUserDetails().collect { details ->
                _userDetails.update { details }
            }
        }
    }

    /**
     * Возвращает поток истории бронирований
     */
    suspend fun getBookingHistory(): Flow<PagingData<BookingHistoryUiItem>> {
        return interactor.getBookingHistory()
            .map {
                it.mapToUi()
            }.cachedIn(viewModelScope)
    }

    private fun PagingData<BookingHistory>.mapToUi(): PagingData<BookingHistoryUiItem> {
        return this.flatMap {
            val date = BookingHistoryUiItem.Date(
                uid = getUUID(),
                date = convertDate(it.date)
            )

            val bookings = it.items.map { booking ->
                BookingHistoryUiItem.Booking(
                    uid = booking.uid,
                    id = booking.id,
                    statusId = booking.statusId,
                    establishment = booking.establishment,
                    startedAt = booking.startedAt,
                    endedAt = booking.endedAt
                )
            }

            listOf(date) + bookings
        }
    }

    private fun convertDate(dateString: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val today = Calendar.getInstance().time
        val yesterday = Calendar.getInstance().apply { this.add(Calendar.DATE, -1) }.time
        val tomorrow = Calendar.getInstance().apply { this.add(Calendar.DATE, 1) }.time

        val todayDate = dateFormat.format(today)
        val yesterdayDate = dateFormat.format(yesterday.time)
        val tomorrowDate = dateFormat.format(tomorrow.time)

        return when (dateString) {
            todayDate -> "Сегодня"
            yesterdayDate -> "Вчера"
            tomorrowDate -> "Завтра"
            else -> {
                val date = dateFormat.parse(dateString) ?: return ""
                val newFormat = SimpleDateFormat("dd MMMM yyyy",  Locale.getDefault())
                newFormat.format(date)
            }
        }
    }

    /**
     * Отменить бронирование
     */
    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _bookingCanceledFlow.emit(interactor.cancelBooking(bookingId))
        }
    }

    /**
     * Удалить бронирование из истории
     */
    fun deleteBooking(bookingId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteBookingFlow.emit(interactor.deleteBooking(bookingId))
        }
    }
}