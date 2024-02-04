package com.example.booking.profile.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.profile.domain.ProfileInteractor
import com.example.booking.profile.domain.SettingsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val interactor: SettingsInteractor
): ViewModel() {
    private val _userDetails: MutableStateFlow<UserDetails> = MutableStateFlow(UserDetails())

    val userDetails: StateFlow<UserDetails>
        get() = _userDetails

    private val _detailsSavedFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()

    val detailsSavedFlow: Flow<Boolean>
        get() = _detailsSavedFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getUserDetails().collect { details ->
                _userDetails.update { details }
            }
        }
    }

    fun saveUserDetails(userDetails: UserDetails) {
        viewModelScope.launch {
            interactor.saveUserDetails(userDetails)
        }
    }
}