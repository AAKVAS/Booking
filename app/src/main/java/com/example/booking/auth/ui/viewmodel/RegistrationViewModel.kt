package com.example.booking.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.data.entity.RegisterResult
import com.example.booking.auth.domain.LoginInteractor
import com.example.booking.auth.domain.model.RegistrationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val interactor: LoginInteractor
) : ViewModel() {
    private val _registrationDetailsFlow: MutableStateFlow<RegistrationDetails> =
        MutableStateFlow(RegistrationDetails())

    val registrationDetailsFlow: StateFlow<RegistrationDetails>
        get() = _registrationDetailsFlow.asStateFlow()

    private val _registeredFlow: MutableSharedFlow<Boolean> = MutableSharedFlow(0)
    val registeredFlow: Flow<Boolean>
        get() = _registeredFlow

    fun setRegistrationDetails(registrationDetails: RegistrationDetails) {
        _registrationDetailsFlow.update { registrationDetails }
    }

    fun register(registrationDetails: RegistrationDetails) {
        viewModelScope.launch {
            val registered = interactor.register(registrationDetails) is RegisterResult.Success
            _registeredFlow.emit(registered)
        }
    }
}