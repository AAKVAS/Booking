package com.example.booking.establishments.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booking.common.data.LoadingState
import com.example.booking.establishments.domain.CatalogInteractor
import com.example.booking.establishments.domain.model.Establishment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [ViewModel] экрана филиала заведения
 */
class EstablishmentViewModel @AssistedInject constructor(
    @Assisted val establishmentId: Long,
    private val interactor: CatalogInteractor
) : ViewModel() {
    @AssistedFactory
    interface EstablishmentViewModelFactory {
        fun create(establishmentId: Long): EstablishmentViewModel
    }

    private val _establishmentFlow: MutableStateFlow<LoadingState<Establishment>> = MutableStateFlow(LoadingState.Loading)
    val establishmentFlow: StateFlow<LoadingState<Establishment>> = _establishmentFlow.asStateFlow()

    private val requireEstablishment: Establishment
        get() = (_establishmentFlow.value as LoadingState.Success<Establishment>).body

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.getEstablishmentFlow(establishmentId)
            _establishmentFlow.update {
                if (result.isFailure) {
                    LoadingState.Failure(RuntimeException("Не удалось получить данные о сервисе"))
                } else {
                    LoadingState.Success(result.getOrNull())
                }
            }
        }
    }

    /**
     * Изменить избранность филиала
     */
    fun changeFavoriteStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val establishment = requireEstablishment
            val favorite = !establishment.favorite
            interactor.setEstablishmentFavorite(establishmentId, favorite)
            _establishmentFlow.update {
                LoadingState.Success(establishment.copy(favorite = favorite))
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: EstablishmentViewModelFactory, establishmentId: Long) : ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(establishmentId) as T
                }
            }
        }
    }
}