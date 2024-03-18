package com.example.booking.profile.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import androidx.paging.map
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.common.ui.viewModel.NetworkViewModel
import com.example.booking.common.utils.getUUID

import com.example.booking.profile.domain.ProfileInteractor
import com.example.booking.profile.domain.model.BookingHistory
import com.example.booking.profile.ui.model.BookingHistoryUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    /**
     * Возвращает поток истории бронирований
     */
    suspend fun getBookingHistory(): Flow<PagingData<BookingHistoryUiItem>> {
        return interactor.getBookingHistory()
            .map {
                it.mapToUi()
            }.cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getUserDetails().collect { details ->
                _userDetails.update { details }
            }
        }
    }

    private fun PagingData<BookingHistory>.mapToUi(): PagingData<BookingHistoryUiItem> {
        return this.flatMap {
            val date = BookingHistoryUiItem.Date(
                uid = getUUID(),
                date = it.date
            )

            val bookings = it.items.map { booking ->
                BookingHistoryUiItem.Booking(
                    uid = booking.uid,
                    id = booking.id,
                    statusId = booking.statusId,
                    status = booking.status,
                    service = booking.service,
                    startedAt = booking.startedAt,
                    endedAt = booking.endedAt
                )
            }

            listOf(date) + bookings
        }
    }
}