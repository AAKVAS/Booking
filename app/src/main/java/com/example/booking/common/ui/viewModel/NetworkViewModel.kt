package com.example.booking.common.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.common.domain.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * [ViewModel] для работы которого необходимо наличие доступа к backend'у
 */
abstract class NetworkViewModel (
    protected open val interactor: NetworkInteractor
) : ViewModel() {
    private val _isServiceAvailable: MutableStateFlow<Boolean> = MutableStateFlow(true)
    /**
     * Доступен ли backend-сервис
     */
    val isServiceAvailable: StateFlow<Boolean>
        get() = _isServiceAvailable

    /**
     * Проверить доступность backend-сервиса
     */
    fun checkServiceAvailable() {
        viewModelScope.launch(Dispatchers.IO) {
            _isServiceAvailable.emit(interactor.isServiceAvailable())
        }
    }
}