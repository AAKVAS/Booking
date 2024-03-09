package com.example.booking.services.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.booking.services.data.datasource.ServiceListAPI
import com.example.booking.services.data.datasource.ServicePagingSource
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.domain.model.Service
import com.example.booking.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ServiceRepositoryImpl(
    private val api: ServiceListAPI
) : ServiceRepository {
    override fun getServices(searchParams: SearchParams): Flow<PagingData<Service>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 1
        ) {
            ServicePagingSource(searchParams, api)
        }.flow

    override suspend fun getServiceDetails(userLogin: String, serviceId: Long): Result<Service> {
        return runCatching {
            api.getServiceDetails(userLogin, serviceId)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}