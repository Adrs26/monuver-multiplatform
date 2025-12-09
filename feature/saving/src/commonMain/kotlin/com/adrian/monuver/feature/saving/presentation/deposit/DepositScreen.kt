package com.adrian.monuver.feature.saving.presentation.deposit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isCreateDepositTransactionSuccess
import com.adrian.monuver.core.domain.util.isEmptyDepositAccount
import com.adrian.monuver.core.domain.util.isEmptyDepositAmount
import com.adrian.monuver.core.domain.util.isEmptyDepositDate
import com.adrian.monuver.core.domain.util.isInsufficientAccountBalance
import com.adrian.monuver.core.domain.util.isTransactionDateAfterToday
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Saving
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.saving.domain.model.DepositWithdraw
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.add
import monuver.feature.saving.generated.resources.add_save_balance
import monuver.feature.saving.generated.resources.amount
import monuver.feature.saving.generated.resources.choose_source_account
import monuver.feature.saving.generated.resources.choose_transaction_date
import monuver.feature.saving.generated.resources.date
import monuver.feature.saving.generated.resources.saving
import monuver.feature.saving.generated.resources.source_account
import org.jetbrains.compose.resources.stringResource

@Composable
fun DepositScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<DepositViewModel>(navController)
    val state by viewModel.state.collectAsStateWithLifecycle()

    DepositContent(
        state = state,
        onAction = { action ->
            when (action) {
                is DepositAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is DepositAction.NavigateToAccount -> {
                    navController.navigate(Saving.DepositAccount)
                }
                is DepositAction.AddDeposit -> {
                    viewModel.createNewTransaction(action.deposit)
                }
            }
        }
    )
}

@Composable
private fun DepositContent(
    state: DepositState,
    onAction: (DepositAction) -> Unit
) {
    val date = rememberTextFieldState(initialText = "")
    val formattedAmount = rememberTextFieldState(initialText = "")

    var rawAmount by remember { mutableLongStateOf(0) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isCreateDepositTransactionSuccess()) {
            onAction(DepositAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.add_save_balance),
                onNavigateBack = {
                    onAction(DepositAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        DepositAction.AddDeposit(
                            DepositWithdraw(
                                savingId = state.savingId,
                                savingName = state.savingName,
                                accountId = state.accountId,
                                accountName = state.accountName,
                                date = date.text.toString(),
                                amount = rawAmount,
                            )
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DisableTextField(
                label = stringResource(Res.string.saving),
                value = state.savingName,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.date),
                placeholder = stringResource(Res.string.choose_transaction_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyDepositDate() || state.result.isTransactionDateAfterToday(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyDepositAmount()
            )
            ClickTextField(
                value = state.accountName,
                label = stringResource(Res.string.source_account),
                placeholder = stringResource(Res.string.choose_source_account),
                onClick = {
                    onAction(DepositAction.NavigateToAccount)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = state.result.isEmptyDepositAccount() || state.result.isInsufficientAccountBalance()
            )
        }
    }

    if (showDatePickerDialog) {
        CommonDatePicker(
            onDateSelected = { selectedDate ->
                val inputDate = DateHelper.formatToLocalDate(selectedDate ?: 0L)
                date.edit { replace(0, length, inputDate.toString()) }
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}