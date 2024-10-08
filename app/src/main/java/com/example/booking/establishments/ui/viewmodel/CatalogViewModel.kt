package com.example.booking.establishments.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.common.ui.viewModel.NetworkViewModel
import com.example.booking.establishments.domain.CatalogInteractor
import com.example.booking.establishments.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] фрагмента со списком всех и избранных услуг
 */
@HiltViewModel
class CatalogViewModel @Inject constructor(
    interactor: CatalogInteractor
) : NetworkViewModel(interactor) {
    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Вошёл ли пользователь в аккаунт
     */
    val isLogged: StateFlow<Boolean>
        get() = _isLogged

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
     * Список городов
     */
    private val _cities: MutableStateFlow<List<City>> = MutableStateFlow(listOf())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLogged.emit(interactor.isUserLogged())
                _cities.emit(interactor.getCities())
            }
            catch (exception: Exception) {}
        }
    }

    fun updateCityId(cityId: Long) {
        _cityId.update { cityId }
    }

    fun updateSearchPattern(searchPattern: String) {
        _searchPattern.update { searchPattern }
    }
}