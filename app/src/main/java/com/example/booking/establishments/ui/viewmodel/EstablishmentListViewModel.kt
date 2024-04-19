package com.example.booking.establishments.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.booking.establishments.domain.CatalogInteractor
import com.example.booking.establishments.domain.model.Establishment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * [ViewModel] списка филиалов заведений
 */
@HiltViewModel
class EstablishmentListViewModel @Inject constructor(
    private val interactor: CatalogInteractor
) : ViewModel() {
    private val _cityId: MutableStateFlow<Long> = MutableStateFlow(-1)

    /**
     * Id города, в котором ищем услуги. Если любой город, то -1
     */
    val cityId: StateFlow<Long>
        get() = _cityId.asStateFlow()

    private val _searchPattern: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Строка, по которой ищутся услуги
     */
    val searchPattern: StateFlow<String>
        get() = _searchPattern.asStateFlow()

    /**
     * Инициирует поиск сервисов
     */
    fun search(searchPattern: String = "", cityId: Long = -1): Flow<PagingData<Establishment>> {
        _searchPattern.update { searchPattern }
        _cityId.update { cityId }
        return interactor.getEstablishments(_searchPattern.value, _cityId.value).cachedIn(viewModelScope)
    }
}