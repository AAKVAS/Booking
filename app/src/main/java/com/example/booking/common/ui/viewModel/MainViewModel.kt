package com.example.booking.common.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.booking.common.domain.MainInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] для главной активности
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel() {
    /**
     * Вошёл ли пользователь в аккаунт
     */
    suspend fun isUserLogged(): Boolean {
        return interactor.isUserLogged()
    }
}