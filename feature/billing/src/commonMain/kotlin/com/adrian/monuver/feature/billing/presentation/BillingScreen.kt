package com.adrian.monuver.feature.billing.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Billing
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BillingScreen(
    navController: NavHostController,
) {
    val viewModel = koinViewModel<BillingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    BillingContent(
        state = state,
        onAction = { action ->
            when (action) {
                is BillingAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is BillingAction.NavigateToAddBill -> {
                    navController.navigate(Billing.Add)
                }
                is BillingAction.NavigateToBillDetail -> {
                    navController.navigate(Billing.Detail(action.billId))
                }
                is BillingAction.SettingsApply -> {
                    viewModel.setReminderSettings(action.reminderState)
                }
            }
        }
    )
}

@Composable
internal expect fun BillingContent(
    state: BillingState,
    onAction: (BillingAction) -> Unit
)

internal suspend fun requestNotificationPermission(
    controller: PermissionsController,
    snackbarHostState: SnackbarHostState,
    onPermissionResult: (Boolean) -> Unit
) {
    try {
        controller.providePermission(Permission.REMOTE_NOTIFICATION)
        snackbarHostState.showSnackbar("Reminder berhasil diterapkan")
        onPermissionResult(true)
    } catch (_: DeniedAlwaysException) {
        onPermissionResult(false)
    } catch (_: DeniedException) {
        onPermissionResult(false)
    }
}