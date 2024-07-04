package com.example.booking

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.booking.bookings.service.BookingNotificationService
import com.example.booking.bookings.service.BookingNotificationService.Companion.WORK_NAME
import com.example.booking.common.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
        requestNotificationPermissions()
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (permissionGranted) {
            startNotificationService()
        }
    }

    private fun requestNotificationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startNotificationService()
        }
    }

    private fun startNotificationService() {
        val workManager = WorkManager.getInstance(this)

        val work = PeriodicWorkRequestBuilder<BookingNotificationService>(30, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, work)
    }
}