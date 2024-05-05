package com.example.booking.establishments.data.datasource


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.booking.establishments.data.entity.SearchParams
import com.example.booking.establishments.data.network.toModel
import com.example.booking.establishments.domain.model.Establishment

/**
 * [PagingSource] для избранных заведений
 */
class FavoriteEstablishmentPagingSource(
    private val searchParams: SearchParams,
    private val api: EstablishmentListAPI
) : PagingSource<Int, Establishment>() {
    override fun getRefreshKey(state: PagingState<Int, Establishment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Establishment> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.fetchFavoriteEstablishments(
                userLogin = searchParams.userLogin,
                cityId = searchParams.cityId,
                searchPattern = searchParams.searchPattern,
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
        private const val INITIAL_PAGE = 0
        private const val DEFAULT_PAGE_SIZE = 10
    }
}