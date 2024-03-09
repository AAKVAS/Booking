package com.example.booking.profile.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.domain.model.UserDetails

import com.example.booking.profile.domain.ProfileInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] для фрагмента профиля пользователя приложения
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {
    private val _userDetails: MutableStateFlow<UserDetails> = MutableStateFlow(UserDetails())

    /**
     * Данные об аккаунте прользователя
     */
    val userDetails: StateFlow<UserDetails>
        get() = _userDetails

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getUserDetails().collect { details ->
                _userDetails.update { details }
            }
        }
    }
}