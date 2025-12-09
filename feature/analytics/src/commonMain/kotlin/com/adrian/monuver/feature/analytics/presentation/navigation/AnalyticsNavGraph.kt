package com.adrian.monuver.feature.analytics.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Analytics
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.feature.analytics.presentation.analyticsTransaction.AnalyticsTransactionScreen

fun NavGraphBuilder.analyticsNavGraph(
    navController: NavHostController
) {
    animatedComposable<Analytics.Transaction> {
        AnalyticsTransactionScreen(navController)
    }
}