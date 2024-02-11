package com.example.booking.profile.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.profile.domain.ProfileInteractor
import com.example.booking.profile.domain.SettingsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] фрагмента с настройками аккаунта
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val interactor: SettingsInteractor
): ViewModel() {
    private val _userDetails: MutableStateFlow<UserDetails> = MutableStateFlow(UserDetails())

    /**
     * Данные аккаунта вошедшего в систему пользователя
     */
    val userDetails: StateFlow<UserDetails>
        get() = _userDetails

    private val _logoutResult: MutableSharedFlow<Result<Unit>> = MutableSharedFlow()

    /**
     * Результат выхода из аккаунта
     */
    val logoutResult: Flow<Result<Unit>>
        get() = _logoutResult

    private val _detailsSavedResult: MutableSharedFlow<Result<Unit>> = MutableSharedFlow()

    /**
     * Результат попытки сохранить данные аккаунта
     */
    val detailsSavedResult: Flow<Result<Unit>>
        get() = _detailsSavedResult

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getUserDetails().collect { details ->
                _userDetails.update { details }
            }
        }
    }

    /**
     * Сохранить данные аккаунта вошедшего в систему пользователя
     */
    fun saveUserDetails(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveUserDetails(userDetails)
        }
    }

    /**
     * Выйти из аккаунта
     */
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            _logoutResult.emit(interactor.logout())
        }
    }
}