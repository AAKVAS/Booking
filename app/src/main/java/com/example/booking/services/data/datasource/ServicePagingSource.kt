package com.example.booking.services.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.data.network.toModel
import com.example.booking.services.domain.model.Service

class ServicePagingSource(
    private val searchParams: SearchParams,
    private val api: ServiceListAPI
) : PagingSource<Int, Service>() {
    override fun getRefreshKey(state: PagingState<Int, Service>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Service> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.fetchServices(
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
        private const val INITIAL_PAGE = 1
        private const val DEFAULT_PAGE_SIZE = 10
    }
}