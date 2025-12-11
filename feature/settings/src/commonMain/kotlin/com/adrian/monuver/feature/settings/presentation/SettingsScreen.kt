package com.adrian.monuver.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.navigation.Settings
import com.adrian.monuver.feature.settings.presentation.components.SettingsDataContent
import com.adrian.monuver.feature.settings.presentation.components.SettingsPreferencesContent
import com.adrian.monuver.feature.settings.presentation.components.SettingsSecurityContent
import com.adrian.monuver.feature.settings.presentation.components.SettingsThemeDialog
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import kotlinx.coroutines.launch
import monuver.feature.settings.generated.resources.Res
import monuver.feature.settings.generated.resources.delete_all_application_data_confirmation
import monuver.feature.settings.generated.resources.settings
import org.jetbrains.compose.resources.stringResource
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
                is SettingsAction.AuthenticationEnableChange -> Unit
            }
        }
    )
}

@Composable
private fun SettingsContent(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showThemeDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onAction(
            SettingsAction.NotificationEnableChange(
                controller.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
            )
        )
    }

    LaunchedEffect(state.result) {
        when (state.result) {
            is DatabaseResultState.BackupDataSuccess -> {
                snackbarHostState.showSnackbar("Data berhasil dicadangkan")
            }
            is DatabaseResultState.BackupDataFailed -> {
                snackbarHostState.showSnackbar("Data gagal dicadangkan")
            }
            is DatabaseResultState.RestoreDataSuccess -> {
                snackbarHostState.showSnackbar("Data berhasil dipulihkan")
            }
            is DatabaseResultState.RestoreDataFailed -> {
                snackbarHostState.showSnackbar("Data gagal dipulihkan")
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.settings),
                onNavigateBack = {
                    onAction(SettingsAction.NavigateBack)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsPreferencesContent(
                isNotificationEnabled = state.isNotificationEnabled,
                themeState = state.themeState,
                onNotificationEnableChange = {
                    scope.launch {
                        if (state.isNotificationEnabled) {
                            controller.openAppSettings()
                        } else {
                            requestNotificationPermission(
                                controller = controller,
                                onPermissionResult = { isGranted ->
                                    if (isGranted) {
                                        onAction(SettingsAction.NotificationEnableChange(true))
                                    } else {
                                        controller.openAppSettings()
                                    }
                                }
                            )
                        }
                    }
                },
                onThemeChange = { showThemeDialog = true },
                modifier = Modifier.padding(top = 8.dp)
            )
            SettingsDataContent(
                onExportData = {
                    onAction(SettingsAction.NavigateToExport)
                },
                onBackupData = {
                    onAction(SettingsAction.BackupData)
                },
                onRestoreData = {
                    onAction(SettingsAction.RestoreData)
                },
                onDeleteData = { showDeleteDialog = true }
            )
            SettingsSecurityContent(
                isAuthenticationEnabled = state.isAuthenticationEnabled,
                onAuthenticationClick = { },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            themeState = state.themeState,
            onThemeChange = { themeState ->
                onAction(SettingsAction.ThemeChange(themeState))
            },
            onDismissRequest = { showThemeDialog = false }
        )
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.delete_all_application_data_confirmation),
            onDismissRequest = { showDeleteDialog = false },
            onConfirmRequest = {
                showDeleteDialog = false
                onAction(SettingsAction.RemoveAllData)
                scope.launch {
                    snackbarHostState.showSnackbar("Semua data berhasil dihapus")
                }
            }
        )
    }
}

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