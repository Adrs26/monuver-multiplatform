package com.adrian.monuver

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.adrian.monuver.feature.transaction.presentation.navigation.transactionNavGraph
import com.adrian.monuver.main.MainScreen

@Composable
fun App() {
    MonuverTheme(
        themeState = ThemeState.System
    ) {
        val rootNavController = rememberNavController()

        NavHost(
            navController = rootNavController,
            startDestination = Starting.Main,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            composable<Starting.Main> { MainScreen(rootNavController) }
            transactionNavGraph(rootNavController)
            budgetingNavGraph(rootNavController)
            analyticsNavGraph(rootNavController)
            accountNavGraph(rootNavController)
            billingNavGraph(rootNavController)
            savingNavGraph(rootNavController)
        }
    }
}