package com.adrian.monuver

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adrian.monuver.core.domain.common.ThemeState
import com.adrian.monuver.core.presentation.navigation.Starting
import com.adrian.monuver.core.presentation.theme.MonuverTheme
import com.adrian.monuver.feature.account.presentation.navigation.accountNavGraph
import com.adrian.monuver.feature.analytics.presentation.navigation.analyticsNavGraph
import com.adrian.monuver.feature.billing.presentation.navigation.billingNavGraph
import com.adrian.monuver.feature.budgeting.presentation.navigation.budgetingNavGraph
import com.adrian.monuver.feature.saving.presentation.navigation.savingNavGraph
import com.adrian.monuver.feature.settings.presentation.navigation.settingsNavGraph
import com.adrian.monuver.feature.transaction.presentation.navigation.transactionNavGraph
import com.adrian.monuver.main.MainScreen
import com.adrian.monuver.onboarding.OnboardingScreen

@Composable
fun App(
    isFirstTime: Boolean = true,
    themeState: ThemeState = ThemeState.System,
    isAuthenticated: Boolean = true,
    onSetFirstTimeToFalse: () -> Unit = {}
) {
    MonuverTheme(
        themeState = themeState
    ) {
        val rootNavController = rememberNavController()

        if (isAuthenticated) {
            NavHost(
                navController = rootNavController,
                startDestination = if (isFirstTime) Starting.Onboarding else Starting.Main,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                startingNavGraph(
                    navController = rootNavController,
                    onSetFirstTimeToFalse = onSetFirstTimeToFalse
                )
                transactionNavGraph(rootNavController)
                budgetingNavGraph(rootNavController)
                analyticsNavGraph(rootNavController)
                accountNavGraph(rootNavController)
                billingNavGraph(rootNavController)
                savingNavGraph(rootNavController)
                settingsNavGraph(rootNavController)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

private fun NavGraphBuilder.startingNavGraph(
    navController: NavHostController,
    onSetFirstTimeToFalse: () -> Unit
) {
    composable<Starting.Onboarding> {
        OnboardingScreen(
            onFinishOnboarding = {
                navController.navigate(Starting.Main) {
                    popUpTo(Starting.Onboarding) {
                        inclusive = true
                    }
                }
                onSetFirstTimeToFalse()
            }
        )
    }

    composable<Starting.Main>(
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {
        MainScreen(
            rootNavController = navController
        )
    }
}