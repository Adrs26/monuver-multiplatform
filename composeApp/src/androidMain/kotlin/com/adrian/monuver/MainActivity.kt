package com.adrian.monuver

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adrian.monuver.core.domain.common.CheckAppVersionState
import com.adrian.monuver.core.domain.common.ThemeState
import com.adrian.monuver.feature.settings.util.AuthenticationManager
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        FileKit.init(this)

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                RequestHighRefreshRate()
            }

            val activity = LocalActivity.current as FragmentActivity

            val viewModel = koinViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val useDarkTheme = when (state.themeState) {
                ThemeState.Light -> false
                ThemeState.Dark -> true
                ThemeState.System -> isSystemInDarkTheme()
            }
            SystemAppearance(useDarkTheme)

            LaunchedEffect(Unit) {
                delay(100)
                if (state.isAuthenticationEnabled) {
                    AuthenticationManager.showBiometricPrompt(
                        activity = activity,
                        onAuthSuccess = { viewModel.setAuthenticationStatus(true) },
                        onAuthFailed = {},
                        onAuthError = {}
                    )
                } else {
                    viewModel.setAuthenticationStatus(true)
                }
            }

            App(
                isFirstTime = state.isFirstTime,
                checkAppVersionState = state.checkAppVersionState,
                themeState = state.themeState,
                isAuthenticated = state.isAuthenticated,
                onCheckAppVersion = viewModel::checkAppVersion,
                onSetFirstTimeToFalse = viewModel::setIsFirstTimeToFalse
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun RequestHighRefreshRate() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val activity = context as? Activity ?: return@LaunchedEffect
        val window = activity.window
        val layoutParams = window.attributes
        val supportedRefreshRates = activity.display?.supportedModes?.map { it.refreshRate } ?: emptyList()
        val highestRefreshRate = supportedRefreshRates.maxOrNull() ?: 60.0f
        layoutParams.preferredRefreshRate = highestRefreshRate
        window.attributes = layoutParams
    }
}

@Composable
private fun SystemAppearance(isDark: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(isDark) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }
}

internal data class MainState(
    val isFirstTime: Boolean = true,
    val checkAppVersionState: CheckAppVersionState = CheckAppVersionState.Check,
    val themeState: ThemeState = ThemeState.System,
    val isAuthenticationEnabled: Boolean = false,
    val isAuthenticated: Boolean = false,
)