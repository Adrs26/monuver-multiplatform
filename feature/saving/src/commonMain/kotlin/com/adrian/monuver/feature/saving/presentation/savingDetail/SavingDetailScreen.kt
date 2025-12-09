package com.adrian.monuver.feature.saving.presentation.savingDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.domain.util.isEmptySavingAmount
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.saving.domain.common.DeleteState
import com.adrian.monuver.feature.saving.presentation.savingDetail.components.RemoveProgressDialog
import com.adrian.monuver.feature.saving.presentation.savingDetail.components.SavingDetailAppBar
import com.adrian.monuver.feature.saving.presentation.savingDetail.components.SavingDetailEmptyListContent
import com.adrian.monuver.feature.saving.presentation.savingDetail.components.SavingDetailListContent
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.complete_save_confirmation
import monuver.feature.saving.generated.resources.delete_save_confirmation
import monuver.feature.saving.generated.resources.settled
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.adrian.monuver.core.presentation.navigation.Saving as SavingRoute

@Composable
fun SavingDetailScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<SavingDetailViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { state ->
        SavingDetailContent(
            state = state,
            onAction = { action ->
                when (action) {
                    is SavingDetailAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is SavingDetailAction.NavigateToEditSaving -> {
                        navController.navigate(SavingRoute.Edit(action.savingId))
                    }
                    is SavingDetailAction.NavigateToDeposit -> {
                        navController.navigate(SavingRoute.Deposit(state.id, state.title))
                    }
                    is SavingDetailAction.NavigateToWithdraw -> {
                        navController.navigate(SavingRoute.Withdraw(state.id, state.title))
                    }
                    is SavingDetailAction.NavigateToTransactionDetail -> {
                        navController.navigate(Transaction.Detail(action.transactionId))
                    }
                    is SavingDetailAction.RemoveSaving -> {
                        viewModel.deleteSaving(action.savingId)
                    }
                    is SavingDetailAction.CompleteSaving -> {
                        viewModel.completeSaving(action.savingId, action.savingName, action.savingAmount)
                    }
                }
            }
        )
    }
}

@Composable
private fun SavingDetailContent(
    state: SavingDetailState,
    onAction: (SavingDetailAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var showRemoveProgressDialog by remember { mutableStateOf(false) }
    var showCompleteConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.progress, state.result) {
        val isSuccess = state.progress is DeleteState.Success

        if (state.progress is DeleteState.Progress) {
            showRemoveProgressDialog = true

            if ((state.progress.current == state.progress.total) || isSuccess) {
                showRemoveProgressDialog = false
                onAction(SavingDetailAction.NavigateBack)
            }
        }

        if (state.result.isEmptySavingAmount()) {
            snackbarHostState.showSnackbar("Tabungan kosong tidak bisa diselesaikan")
        }
    }

    Scaffold(
        topBar = {
            SavingDetailAppBar(
                isActive = state.isActive,
                onNavigateBack = {
                    onAction(SavingDetailAction.NavigateBack)
                },
                onNavigateToEditSaving = {
                    onAction(SavingDetailAction.NavigateToEditSaving(state.id))
                },
                onRemoveSaving = { showRemoveConfirmationDialog = true }
            )
        },
        bottomBar = {
            if (state.isActive) {
                PrimaryActionButton(
                    label = stringResource(Res.string.settled),
                    onClick = { showCompleteConfirmationDialog = true }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        when  {
            state.transactions.isEmpty() -> {
                SavingDetailEmptyListContent(
                    saving = Saving(
                        id = state.id,
                        title = state.title,
                        targetDate = state.targetDate,
                        targetAmount = state.targetAmount,
                        currentAmount = state.currentAmount,
                        isActive = state.isActive
                    ),
                    onNavigateToDeposit = {
                        onAction(SavingDetailAction.NavigateToDeposit)
                    },
                    onNavigateToWithdraw = {
                        onAction(SavingDetailAction.NavigateToWithdraw)
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
            else -> {
                SavingDetailListContent(
                    saving = Saving(
                        id = state.id,
                        title = state.title,
                        targetDate = state.targetDate,
                        targetAmount = state.targetAmount,
                        currentAmount = state.currentAmount,
                        isActive = state.isActive
                    ),
                    transactions = state.transactions,
                    onNavigateToDeposit = {
                        onAction(SavingDetailAction.NavigateToDeposit)
                    },
                    onNavigateToWithdraw = {
                        onAction(SavingDetailAction.NavigateToWithdraw)
                    },
                    onNavigateToTransactionDetail = { transactionId ->
                        onAction(SavingDetailAction.NavigateToTransactionDetail(transactionId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    if (showRemoveConfirmationDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.delete_save_confirmation),
            onDismissRequest = { showRemoveConfirmationDialog = false },
            onConfirmRequest = {
                showRemoveConfirmationDialog = false
                onAction(SavingDetailAction.RemoveSaving(state.id))
                if (state.transactions.isEmpty()) {
                    onAction(SavingDetailAction.NavigateBack)
                }
            }
        )
    }

    if (showRemoveProgressDialog) {
        RemoveProgressDialog(
            onDismissRequest = { showRemoveProgressDialog = false }
        )
    }

    if (showCompleteConfirmationDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.complete_save_confirmation),
            onDismissRequest = { showCompleteConfirmationDialog = false },
            onConfirmRequest = {
                showCompleteConfirmationDialog = false
                onAction(
                    SavingDetailAction.CompleteSaving(
                        savingId = state.id,
                        savingName = state.title,
                        savingAmount = state.currentAmount
                    )
                )
            }
        )
    }
}