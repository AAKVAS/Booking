package com.example.booking.establishments.domain

import androidx.paging.PagingData
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.common.domain.NetworkInteractor
import com.example.booking.common.domain.repository.RemoteRepository
import com.example.booking.establishments.data.entity.SearchParams
import com.example.booking.establishments.domain.model.City
import com.example.booking.establishments.domain.model.Establishment
import com.example.booking.establishments.domain.repository.EstablishmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Интерактор каталога услуг
 */
class CatalogInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val establishmentRepository: EstablishmentRepository,
    remoteRepository: RemoteRepository
) : NetworkInteractor(remoteRepository) {
    /**
     * Вошёл ли пользователь в систему
     */
    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }

    /**
     * Получить список услуг, отфильтровав их по названию и id города, где они предоставляются.
     * Если любой город, то передавать -1
     */
     fun getEstablishments(searchPattern: String, cityId: Long): Flow<PagingData<Establishment>> {
        val searchParams = SearchParams(
            userLogin = "",
            searchPattern = searchPattern,
            cityId = cityId
        )
        return establishmentRepository.getEstablishments(searchParams)
    }

    /**
     * Получить список избранных услуг, отфильтровав их по названию и id города, где они предоставляются.
     * Если любой город, то передавать -1
     */
    suspend fun getFavoriteEstablishments(searchPattern: String, cityId: Long): Flow<PagingData<Establishment>> {
        val searchParams = SearchParams(
            userLogin = getUserLogin(),
            searchPattern = searchPattern,
            cityId = cityId
        )
        return establishmentRepository.getFavoriteEstablishments(searchParams)
    }

    /**
     * Возвращает информацию о филиале заведения
     */
    suspend fun getEstablishmentFlow(establishmentId: Long): Result<Establishment> {
        val userLogin: String = getUserLogin()
        return establishmentRepository.getEstablishmentDetails(userLogin, establishmentId)
    }

    /**
     * Изменяет статус избранности заведения
     */
    suspend fun setEstablishmentFavorite(establishmentId: Long, favorite: Boolean) {
        establishmentRepository.setEstablishmentFavorite(getUserLogin(), establishmentId, favorite)
    }

    /**
     * Получить список городов
     */
    suspend fun getCities(): List<City> {
       return establishmentRepository.getCities()
    }

    private suspend fun getUserLogin(): String {
        return if (loginRepository.isUserLogged()) {
            loginRepository.getUserDetails().first().login
        } else {
            ""
        }
    }
}