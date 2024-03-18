package com.example.booking.services.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.booking.services.domain.CatalogInteractor
import com.example.booking.services.domain.model.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val interactor: CatalogInteractor
) : ViewModel() {
    private val _cityId: MutableStateFlow<Long> = MutableStateFlow(-1)
    private val _searchPattern: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Инициирует поиск сервисов
     */
    suspend fun search(searchPattern: String = "", cityId: Long = -1): Flow<PagingData<Service>> {
        _searchPattern.update { searchPattern }
        _cityId.update { cityId }
        return interactor.getFavoriteServices(_searchPattern.value, _cityId.value).cachedIn(viewModelScope)
    }
}