package com.adrian.monuver.feature.transaction.presentation.transfer

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
import com.adrian.monuver.core.domain.util.SelectAccountType
import com.adrian.monuver.core.domain.util.isCreateTransactionSuccess
import com.adrian.monuver.core.domain.util.isEmptyTransactionAmount
import com.adrian.monuver.core.domain.util.isEmptyTransactionDate
import com.adrian.monuver.core.domain.util.isEmptyTransactionDestination
import com.adrian.monuver.core.domain.util.isEmptyTransactionSource
import com.adrian.monuver.core.domain.util.isInsufficientAccountBalance
import com.adrian.monuver.core.domain.util.isTransactionDateAfterToday
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.transaction.domain.model.Transfer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monuver.feature.transaction.generated.resources.Res
import monuver.feature.transaction.generated.resources.amount
import monuver.feature.transaction.generated.resources.choose_destination_account
import monuver.feature.transaction.generated.resources.choose_source_account
import monuver.feature.transaction.generated.resources.choose_transaction_date
import monuver.feature.transaction.generated.resources.date
import monuver.feature.transaction.generated.resources.destination_account
import monuver.feature.transaction.generated.resources.source_account
import monuver.feature.transaction.generated.resources.transfer
import monuver.feature.transaction.generated.resources.transfer_account
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun TransferScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<TransferViewModel>(navController)
    val state by viewModel.state.collectAsStateWithLifecycle()

    TransferContent(
        state = state,
        onAction = { action ->
            when (action) {
                is TransferAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is TransferAction.NavigateToSourceAccount -> {
                    navController.navigate(Transaction.TransferAccount(SelectAccountType.SOURCE))
                }
                is TransferAction.NavigateToDestinationAccount -> {
                    navController.navigate(Transaction.TransferAccount(SelectAccountType.DESTINATION))
                }
                is TransferAction.AddNewTransfer -> {
                    viewModel.createNewTransfer(action.transfer)
                }
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
@Composable
internal fun TransferContent(
    state: TransferState,
    onAction: (TransferAction) -> Unit
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val date = rememberTextFieldState(initialText = today.toString())
    val formattedAmount = rememberTextFieldState(initialText = "")

    var rawAmount by remember { mutableLongStateOf(0) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isCreateTransactionSuccess()) {
            onAction(TransferAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.transfer_account),
                onNavigateBack = {
                    onAction(TransferAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.transfer),
                onClick = {
                    onAction(
                        TransferAction.AddNewTransfer(
                            Transfer(
                                sourceId = state.accountSourceId,
                                sourceName = state.accountSourceName,
                                destinationId = state.accountDestinationId,
                                destinationName = state.accountDestinationName,
                                date = date.text.toString(),
                                amount = rawAmount
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
            ClickTextField(
                value = state.accountSourceName,
                label = stringResource(Res.string.source_account),
                placeholder = stringResource(Res.string.choose_source_account),
                onClick = {
                    onAction(TransferAction.NavigateToSourceAccount)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = state.result.isEmptyTransactionSource() ||
                    state.result.isInsufficientAccountBalance(),
            )
            ClickTextField(
                value = state.accountDestinationName,
                label = stringResource(Res.string.destination_account),
                placeholder = stringResource(Res.string.choose_destination_account),
                onClick = {
                    onAction(TransferAction.NavigateToDestinationAccount)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyTransactionDestination()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.date),
                placeholder = stringResource(Res.string.choose_transaction_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyTransactionDate() || state.result.isTransactionDateAfterToday(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = state.result.isEmptyTransactionAmount()
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