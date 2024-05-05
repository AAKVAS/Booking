package com.example.booking.establishments.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.booking.establishments.data.datasource.EstablishmentListAPI
import com.example.booking.establishments.data.datasource.EstablishmentPagingSource
import com.example.booking.establishments.data.datasource.FavoriteEstablishmentPagingSource
import com.example.booking.establishments.data.entity.SearchParams
import com.example.booking.establishments.data.network.toModel
import com.example.booking.establishments.domain.model.City
import com.example.booking.establishments.domain.model.Establishment
import com.example.booking.establishments.domain.repository.EstablishmentRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Реализация репозитория заведений
 */
class EstablishmentRepositoryImpl(
    private val api: EstablishmentListAPI
) : EstablishmentRepository {
    override fun getEstablishments(searchParams: SearchParams): Flow<PagingData<Establishment>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 0
        ) {
            EstablishmentPagingSource(searchParams, api)
        }.flow

    override fun getFavoriteEstablishments(searchParams: SearchParams): Flow<PagingData<Establishment>> =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = 5),
            initialKey = 0
        ) {
            FavoriteEstablishmentPagingSource(searchParams, api)
        }.flow

    override suspend fun getEstablishmentDetails(userLogin: String, establishmentId: Long): Result<Establishment> {
        return runCatching {
            api.getEstablishmentDetails(userLogin, establishmentId).toModel()
        }
    }

    override suspend fun setEstablishmentFavorite(userLogin: String, establishmentId: Long, favorite: Boolean) {
        val jsonObject = JSONObject()
        jsonObject.put("userLogin", userLogin)
        jsonObject.put("establishmentId", establishmentId)
        jsonObject.put("favorite", favorite)

        val jsonString = jsonObject.toString()
        val jsonBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        api.setEstablishmentFavorite(jsonBody)
    }

    override suspend fun getCities(): List<City> {
        return api.getCities().map { it.toModel() }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}