package com.example.booking.establishments.domain.repository

import androidx.paging.PagingData
import com.example.booking.establishments.data.entity.SearchParams
import com.example.booking.establishments.domain.model.City
import com.example.booking.establishments.domain.model.Establishment
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с заведениями
 */
interface EstablishmentRepository {
    /**
     * Получить список филиалов заведений
     */
    fun getEstablishments(searchParams: SearchParams): Flow<PagingData<Establishment>>

    /**
     * Получить список избранных филиалов заведений
     */
    fun getFavoriteEstablishments(searchParams: SearchParams): Flow<PagingData<Establishment>>

    /**
     * Получить сведения о филиале заведения по [establishmentId]
     */
    suspend fun getEstablishmentDetails(userLogin: String, establishmentId: Long): Result<Establishment>

    /**
     * Изменить статус избранности филиала заведения
     */
    suspend fun setEstablishmentFavorite(userLogin: String, establishmentId: Long, favorite: Boolean)

    /**
     * Получить список городов
     */
    suspend fun getCities(): List<City>
}