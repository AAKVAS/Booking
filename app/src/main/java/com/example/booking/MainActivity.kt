package com.example.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.example.booking.common.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var _navController: NavController
    val navController: NavController
        get() = _navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        lifecycleScope.launch {
            if (!viewModel.isUserLogged()) {
                _navController.navigate(R.id.loginFragment)
            } else {
                _navController.navigate(R.id.serviceListFragment)
            }
        }
    }
}