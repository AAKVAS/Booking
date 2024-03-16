package com.example.booking.common.domain.repository


/**
 * Общий удалённый репозиторий для проверки доступности backend'а
 */
interface RemoteRepository  {
    /**
     * Доступен ли backend-сервис
     */
    suspend fun isServiceAvailable(): Boolean
}