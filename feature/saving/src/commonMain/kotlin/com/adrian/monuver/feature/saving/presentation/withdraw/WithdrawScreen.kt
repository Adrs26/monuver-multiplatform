package com.adrian.monuver.feature.saving.presentation.withdraw

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
import com.adrian.monuver.core.domain.util.isCreateWithdrawTransactionSuccess
import com.adrian.monuver.core.domain.util.isEmptyWithdrawAccount
import com.adrian.monuver.core.domain.util.isEmptyWithdrawAmount
import com.adrian.monuver.core.domain.util.isEmptyWithdrawDate
import com.adrian.monuver.core.domain.util.isInsufficientSavingBalance
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
import monuver.feature.saving.generated.resources.choose_destination_account
import monuver.feature.saving.generated.resources.choose_transaction_date
import monuver.feature.saving.generated.resources.date
import monuver.feature.saving.generated.resources.destination_account
import monuver.feature.saving.generated.resources.saving
import org.jetbrains.compose.resources.stringResource

@Composable
fun WithdrawScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<WithdrawViewModel>(navController)
    val state by viewModel.state.collectAsStateWithLifecycle()

    WithdrawContent(
        state = state,
        onAction = { action ->
            when (action) {
                is WithdrawAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is WithdrawAction.NavigateToAccount -> {
                    navController.navigate(Saving.WithdrawAccount)
                }
                is WithdrawAction.AddWithdraw -> {
                    viewModel.createNewTransaction(action.withdraw)
                }
            }
        }
    )
}

@Composable
private fun WithdrawContent(
    state: WithdrawState,
    onAction: (WithdrawAction) -> Unit
) {
    val date = rememberTextFieldState(initialText = "")
    val formattedAmount = rememberTextFieldState(initialText = "")

    var rawAmount by remember { mutableLongStateOf(0) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isCreateWithdrawTransactionSuccess()) {
            onAction(WithdrawAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.add_save_balance),
                onNavigateBack = {
                    onAction(WithdrawAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        WithdrawAction.AddWithdraw(
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
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                errorMessage = stringResource(state.result.toStringRes()),
                isError = state.result.isInsufficientSavingBalance()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.date),
                placeholder = stringResource(Res.string.choose_transaction_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyWithdrawDate() || state.result.isTransactionDateAfterToday(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyWithdrawAmount()
            )
            ClickTextField(
                value = state.accountName,
                label = stringResource(Res.string.destination_account),
                placeholder = stringResource(Res.string.choose_destination_account),
                onClick = {
                    onAction(WithdrawAction.NavigateToAccount)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = state.result.isEmptyWithdrawAccount()
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