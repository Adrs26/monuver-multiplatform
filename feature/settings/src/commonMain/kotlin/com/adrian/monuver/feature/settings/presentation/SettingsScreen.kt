package com.adrian.monuver.feature.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Settings
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onAction = { action ->
            when (action) {
                is SettingsAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is SettingsAction.NotificationEnableChange -> {
                    viewModel.setNotification(action.isEnabled)
                }
                is SettingsAction.ThemeChange -> {
                    viewModel.changeTheme(action.themeState)
                }
                is SettingsAction.NavigateToExport -> {
                    navController.navigate(Settings.Export)
                }
                is SettingsAction.BackupData -> {
                    viewModel.backupData()
                }
                is SettingsAction.RestoreData -> {
                    viewModel.restoreData()
                }
                is SettingsAction.RemoveAllData -> {
                    viewModel.deleteAllData()
                }
                is SettingsAction.AuthenticationEnableChange -> {
                    viewModel.setAuthentication(action.isEnabled)
                }
            }
        }
    )
}

@Composable
internal expect fun SettingsContent(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
)

internal suspend fun requestNotificationPermission(
    controller: PermissionsController,
    onPermissionResult: (Boolean) -> Unit
) {
    try {
        controller.providePermission(Permission.REMOTE_NOTIFICATION)
        onPermissionResult(true)
    } catch (_: DeniedAlwaysException) {
        onPermissionResult(false)
    } catch (_: DeniedException) {
        onPermissionResult(false)
    }
}