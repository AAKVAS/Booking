package com.example.booking.services.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.services.domain.CatalogInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServiceListViewModel @Inject constructor(
    private val interactor: CatalogInteractor
) : ViewModel() {
    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isLogged: MutableStateFlow<Boolean>
        get() = _isLogged

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLogged.emit(interactor.isUserLogged())
        }
    }
}