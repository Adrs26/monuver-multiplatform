package com.adrian.monuver.feature.billing.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrian.monuver.core.presentation.components.CommonFloatingActionButton
import com.adrian.monuver.feature.billing.presentation.components.BillAppBar
import com.adrian.monuver.feature.billing.presentation.components.BillReminderDialog
import com.adrian.monuver.feature.billing.presentation.components.BillTabRowWithPager
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch

@Composable
internal actual fun BillingContent(
    state: BillingState,
    onAction: (BillingAction) -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showReminderDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BillAppBar(
                onNavigateBack = {
                    onAction(BillingAction.NavigateBack)
                },
                onReminderClick = { showReminderDialog = true }
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                onAction(BillingAction.NavigateToAddBill)
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        BillTabRowWithPager(
            pendingBills = state.pendingBills,
            dueBills = state.dueBills,
            paidBills = state.paidBills.collectAsLazyPagingItems(),
            onNavigateToBillDetail = { billId ->
                onAction(BillingAction.NavigateToBillDetail(billId))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

    if (showReminderDialog) {
        BillReminderDialog(
            reminderDaysBeforeDue = state.reminderDaysBeforeDue,
            isReminderBeforeDueDayEnabled = state.isReminderBeforeDueDayEnabled,
            isReminderForDueBillEnabled = state.isReminderForDueBillEnabled,
            onDismissRequest = { showReminderDialog = false },
            onSettingsApply = { reminderState ->
                scope.launch {
                    requestNotificationPermission(
                        controller = controller,
                        snackbarHostState = snackbarHostState,
                        onPermissionResult = { isGranted ->
                            if (isGranted) {
                                onAction(BillingAction.SettingsApply(reminderState))
                            } else {
                                controller.openAppSettings()
                            }
                        }
                    )
                }
            }
        )
    }
}