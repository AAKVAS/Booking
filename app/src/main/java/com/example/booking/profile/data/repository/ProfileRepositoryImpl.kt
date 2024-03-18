package com.example.booking.profile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.booking.profile.data.datasource.BookingHistoryPagingSource
import com.example.booking.profile.data.datasource.ProfileAPI
import com.example.booking.profile.domain.model.BookingHistory
import com.example.booking.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileAPI
) : ProfileRepository {
    override fun getBookingHistory(userLogin: String): Flow<PagingData<BookingHistory>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 1
        ) {
            BookingHistoryPagingSource(userLogin, api)
        }.flow

    companion object {
        private const val PAGE_SIZE = 10
    }
}