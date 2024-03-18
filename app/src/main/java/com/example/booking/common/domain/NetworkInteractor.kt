package com.example.booking.common.domain

import com.example.booking.common.domain.repository.RemoteRepository

/**
 * Базовый [Interactor] для взаимодействия с backend'ом
 */
open class NetworkInteractor(
    private val remoteRepository: RemoteRepository
) {
    /**
     * Доступен ли backend-сервис
     */
    suspend fun isServiceAvailable(): Boolean {
        return remoteRepository.isServiceAvailable()
    }
}