package com.example.booking.services.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.booking.common.utils.getUUID
import com.example.booking.services.data.datasource.FavoriteServicePagingSource
import com.example.booking.services.data.datasource.ServiceListApiImpl
import com.example.booking.services.data.datasource.ServicePagingSource
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.domain.model.City
import com.example.booking.services.domain.model.Service
import com.example.booking.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow

class ServiceRepositoryImpl(
    private val api: ServiceListApiImpl
) : ServiceRepository {
    override fun getServices(searchParams: SearchParams): Flow<PagingData<Service>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 1
        ) {
            ServicePagingSource(searchParams, api)
        }.flow

    override fun getFavoriteServices(searchParams: SearchParams): Flow<PagingData<Service>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 1
        ) {
            FavoriteServicePagingSource(searchParams, api)
        }.flow

    override suspend fun getServiceDetails(userLogin: String, serviceId: Long): Result<Service> {
        return runCatching {
            api.getServiceDetails(userLogin, serviceId)
        }
    }

    override suspend fun setServiceFavorite(userLogin: String, serviceId: Long, favorite: Boolean) {
        //api.setServiceFavorite(userLogin, serviceId, favorite)
    }

    override suspend fun getCities(): List<City> {
        return api.getCities()
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}