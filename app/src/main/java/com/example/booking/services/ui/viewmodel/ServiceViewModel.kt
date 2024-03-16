package com.example.booking.services.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booking.common.data.LoadingState
import com.example.booking.services.domain.CatalogInteractor
import com.example.booking.services.domain.model.Service
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceViewModel @AssistedInject constructor(
    @Assisted val serviceId: Long,
    private val interactor: CatalogInteractor
) : ViewModel() {
    @AssistedFactory
    interface ServiceViewModelFactory {
        fun create(serviceId: Long): ServiceViewModel
    }

    private val _serviceFlow: MutableStateFlow<LoadingState<Service>> = MutableStateFlow(LoadingState.Loading)
    val serviceFlow: StateFlow<LoadingState<Service>> = _serviceFlow.asStateFlow()

    private val requireService: Service
        get() = (_serviceFlow.value as LoadingState.Success<Service>).body

    init {
        viewModelScope.launch {
            val result = interactor.getServiceFlow(serviceId)
            _serviceFlow.update {
                if (result.isFailure) {
                    LoadingState.Failure(RuntimeException("Не удалось получить данные о сервисе"))
                } else {
                    LoadingState.Success(result.getOrNull())
                }
            }
        }
    }

    fun changeFavoriteStatus() {
        viewModelScope.launch {
            val service = requireService
            val favorite = !service.favorite
            interactor.setServiceFavorite(serviceId, favorite)
            _serviceFlow.update {
                LoadingState.Success(service.copy(favorite = favorite))
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: ServiceViewModelFactory, serviceId: Long) : ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(serviceId) as T
                }
            }
        }
    }
}