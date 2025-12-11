package com.adrian.monuver.feature.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Settings
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.feature.settings.presentation.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController
) {
    animatedComposable<Settings.Main> {
        SettingsScreen(navController)
    }
}