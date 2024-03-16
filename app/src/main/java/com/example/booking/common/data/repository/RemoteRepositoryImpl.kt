package com.example.booking.common.data.repository

import com.example.booking.common.data.datasource.PingAPI
import com.example.booking.common.domain.repository.RemoteRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: PingAPI
) : RemoteRepository {
    override suspend fun isServiceAvailable(): Boolean {
        val result = runCatching {
            api.ping()
        }
        return result.isSuccess
    }
}