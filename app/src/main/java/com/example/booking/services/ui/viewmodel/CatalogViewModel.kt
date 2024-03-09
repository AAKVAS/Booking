package com.example.booking.services.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.booking.services.domain.CatalogInteractor
import com.example.booking.services.domain.model.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val interactor: CatalogInteractor
) : ViewModel() {
    private val _serviceList: MutableStateFlow<PagingData<Service>> = MutableStateFlow(PagingData.empty())

    private var searchJob: Job? = null

    /**
     * [Flow] списка услуг
     */
    val serviceList: StateFlow<PagingData<Service>>
        get() = _serviceList.asStateFlow()


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
    suspend fun search(searchPattern: String = "", cityId: Long = -1): Flow<PagingData<Service>> {
        _searchPattern.update { searchPattern }
        _cityId.update { cityId }
        return interactor.getServices(_searchPattern.value, _cityId.value).cachedIn(viewModelScope)
    }
}