package com.example.booking.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.domain.LoginInteractor
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.RegistrationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] экрана регистрации
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val interactor: LoginInteractor
) : ViewModel() {
    private val _registrationDetailsFlow: MutableStateFlow<RegistrationDetails> =
        MutableStateFlow(RegistrationDetails())

    /**
     * Состояние данных для регистрации
     */
    val registrationDetailsFlow: StateFlow<RegistrationDetails>
        get() = _registrationDetailsFlow.asStateFlow()

    private val _registeredFlow: MutableSharedFlow<Boolean> = MutableSharedFlow(0)

    /**
     * Результат попытки зарегестрироваться в системе
     */
    val registeredFlow: Flow<Boolean>
        get() = _registeredFlow

    /**
     * Изменить состояние данных для регистрации
     */
    fun setRegistrationDetails(registrationDetails: RegistrationDetails) {
        _registrationDetailsFlow.update { registrationDetails }
    }

    /**
     * Попытаться зарегестрироваться в системе
     */
    fun register(registrationDetails: RegistrationDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            val registered = interactor.register(registrationDetails) is RegisterResult.Success
            _registeredFlow.emit(registered)
        }
    }
}