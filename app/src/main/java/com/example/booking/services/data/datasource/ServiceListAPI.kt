package com.example.booking.services.data.datasource

import com.example.booking.services.domain.model.Service

interface ServiceListAPI {
    suspend fun fetchServices(
        userLogin: String,
        cityId: Long,
        searchPattern: String,
        page: Int,
        size: Int,
    ): List<Service>

    suspend fun getServiceDetails(userLogin: String, serviceId: Long): Service
}