package com.example.booking.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.domain.LoginInteractor
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.common.ui.viewModel.NetworkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] экрана входа
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    override val interactor: LoginInteractor
) : NetworkViewModel(interactor) {
    private val _loginDetailsState: MutableStateFlow<LoginDetails> =
        MutableStateFlow(LoginDetails("", ""))

    private val _loggedFlow: MutableSharedFlow<Boolean> = MutableSharedFlow(0)

    /**
     * Состояние, вошёл ли пользователь в профиль
     */
    val loggedFlow: Flow<Boolean>
        get() = _loggedFlow

    /**
     * Состояние данных для входа
     */
    val loginDetailsState: StateFlow<LoginDetails>
        get() = _loginDetailsState

    /**
     *  Вход в систему
     */
    fun login(loginDetails: LoginDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            if (interactor.isServiceAvailable()) {
                _loggedFlow.emit(interactor.login(loginDetails))
            } else {
                isServiceAvailableStateFlow.emit(false)
            }
        }
    }
}