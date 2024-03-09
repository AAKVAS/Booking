package com.example.booking.services.domain

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.domain.repository.ServiceRepository
import com.example.booking.services.domain.model.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Интерактор каталога услуг
 */
class CatalogInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val serviceRepository: ServiceRepository
) {
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
    suspend fun getServices(searchPattern: String, cityId: Long): Flow<PagingData<Service>> {
        val userLogin: String = getUserLogin()
        val searchParams = SearchParams(
            userLogin = userLogin,
            searchPattern = searchPattern,
            cityId = cityId
        )
        return serviceRepository.getServices(searchParams)
    }

    private suspend fun getUserLogin(): String {
        return if (loginRepository.isUserLogged()) {
            loginRepository.getUserDetails().first().login
        } else {
            ""
        }
    }

    suspend fun getServiceFlow(serviceId: Long): Result<Service> {
        val userLogin: String = getUserLogin()
        return serviceRepository.getServiceDetails(userLogin, serviceId)
    }
}