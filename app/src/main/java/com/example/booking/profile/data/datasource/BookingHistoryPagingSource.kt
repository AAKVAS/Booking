package com.example.booking.profile.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.booking.profile.data.network.toModel
import com.example.booking.profile.domain.model.BookingHistory

/**
 * [PagingSource] списка истории бронирований
 */
class BookingHistoryPagingSource (
    private val userLogin: String,
    private val api: ProfileAPI
) : PagingSource<Int, BookingHistory>()  {
    override fun getRefreshKey(state: PagingState<Int, BookingHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookingHistory> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.getBookingHistory(
                userLogin = userLogin,
                page = page,
                size = DEFAULT_PAGE_SIZE
            ).map { it.toModel() }

            return LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_PAGE = 1
        private const val DEFAULT_PAGE_SIZE = 10
    }
}