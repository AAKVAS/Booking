package com.example.booking.common.data.repository

import com.example.booking.common.data.datasource.PingAPI
import com.example.booking.common.domain.repository.RemoteRepository
import javax.inject.Inject

/**
 * Реализация удалённого репозитория для проверки доступа к backend'у
 */
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