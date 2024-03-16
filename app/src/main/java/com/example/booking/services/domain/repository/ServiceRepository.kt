package com.example.booking.services.domain.repository

import androidx.paging.PagingData
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.domain.model.City
import com.example.booking.services.domain.model.Service
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий, работающий с данными об услугах
 */
interface ServiceRepository {
    /**
     * Получить список услуг, отфильтровав их по названию и id города, где они предоставляются.
     * Если любой город, то передавать -1
     */
    fun getServices(searchParams: SearchParams): Flow<PagingData<Service>>

    /**
     * Получить список избранных услуг, отфильтровав их по названию и id города, где они предоставляются.
     * Если любой город, то передавать -1
     */
    fun getFavoriteServices(searchParams: SearchParams): Flow<PagingData<Service>>

    /**
     * Получить сведения об услуге по [serviceId]
     */
    suspend fun getServiceDetails(userLogin: String, serviceId: Long): Result<Service>

    /**
     * Изменить статус избранности сервиса
     */
    suspend fun setServiceFavorite(userLogin: String, serviceId: Long, favorite: Boolean)

    /**
     * Получить список городов
     */
    suspend fun getCities(): List<City>
}