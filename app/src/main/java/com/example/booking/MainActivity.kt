package com.example.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.booking.common.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Главная активность приложения
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var _navController: NavController

    /**
     * [NavController] для осуществления навигации в приложении
     */
    val navController: NavController
        get() = _navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setMainScreen()
    }

    private fun setMainScreen() {
        _navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        lifecycleScope.launch {
            if (!viewModel.isUserLogged()) {
                _navController.navigate(R.id.loginFragment)
            } else {
                _navController.navigate(R.id.action_catalogFragment_root)
            }
        }
    }
}