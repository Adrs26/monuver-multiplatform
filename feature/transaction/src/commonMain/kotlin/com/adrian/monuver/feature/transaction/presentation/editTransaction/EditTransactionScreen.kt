package com.adrian.monuver.feature.transaction.presentation.editTransaction

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
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.domain.util.isEmptyTransactionAmount
import com.adrian.monuver.core.domain.util.isEmptyTransactionCategory
import com.adrian.monuver.core.domain.util.isEmptyTransactionDate
import com.adrian.monuver.core.domain.util.isEmptyTransactionTitle
import com.adrian.monuver.core.domain.util.isInsufficientAccountBalance
import com.adrian.monuver.core.domain.util.isTransactionDateAfterToday
import com.adrian.monuver.core.domain.util.isUpdateTransactionSuccess
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.transaction.domain.model.EditTransaction
import monuver.feature.transaction.generated.resources.Res
import monuver.feature.transaction.generated.resources.amount
import monuver.feature.transaction.generated.resources.category
import monuver.feature.transaction.generated.resources.choose_transaction_category
import monuver.feature.transaction.generated.resources.choose_transaction_date
import monuver.feature.transaction.generated.resources.date
import monuver.feature.transaction.generated.resources.destination_account
import monuver.feature.transaction.generated.resources.edit_transaction
import monuver.feature.transaction.generated.resources.enter_transaction_title
import monuver.feature.transaction.generated.resources.save
import monuver.feature.transaction.generated.resources.source_account
import monuver.feature.transaction.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Composable
fun EditTransactionScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<EditTransactionViewModel>(navController) {
        parametersOf(navBackStackEntry.savedStateHandle)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()

    state?.let { state ->
        EditTransactionContent(
            state = state,
            result = result,
            onAction = { action ->
                when (action) {
                    is EditTransactionAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditTransactionAction.NavigateToCategory -> {
                        navController.navigate(Transaction.EditCategory(action.type))
                    }
                    is EditTransactionAction.EditCurrentTransaction -> {
                        viewModel.updateTransaction(action.transaction)
                    }
                }
            }
        )
    }
}

@Composable
private fun EditTransactionContent(
    state: EditTransactionState,
    result: DatabaseResultState,
    onAction: (EditTransactionAction) -> Unit
) {
    val title = rememberTextFieldState(initialText = state.title)
    val date = rememberTextFieldState(initialText = state.date)
    val formattedAmount = rememberTextFieldState(initialText = state.amount.toFormattedAmount())

    var rawAmount by remember { mutableLongStateOf(state.amount) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result.isUpdateTransactionSuccess()) {
            onAction(EditTransactionAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.edit_transaction),
                onNavigateBack = {
                    onAction(EditTransactionAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.save),
                onClick = {
                    onAction(
                        EditTransactionAction.EditCurrentTransaction(
                            EditTransaction(
                                id = state.id,
                                title = title.text.toString(),
                                type = state.type,
                                parentCategory = state.parentCategory,
                                childCategory = state.childCategory,
                                initialParentCategory = state.initialParentCategory,
                                date = date.text.toString(),
                                initialDate = state.initialDate,
                                amount = rawAmount,
                                initialAmount = state.initialAmount,
                                sourceId = state.accountId,
                                sourceName = state.accountName,
                                isLocked = state.isLocked
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
            CommonTextField(
                state = title,
                label = stringResource(Res.string.title),
                placeholder = stringResource(Res.string.enter_transaction_title),
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = result.isEmptyTransactionTitle()
            )
            ClickTextField(
                value = stringResource(DatabaseCodeMapper.toChildCategoryTitle(state.childCategory)),
                label = stringResource(Res.string.category),
                placeholder = stringResource(Res.string.choose_transaction_category),
                onClick = {
                    onAction(EditTransactionAction.NavigateToCategory(state.type))
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyTransactionCategory()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.date),
                placeholder = stringResource(Res.string.choose_transaction_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyTransactionDate() || result.isTransactionDateAfterToday(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyTransactionAmount()
            )
            DisableTextField(
                label = if (state.type == TransactionType.INCOME)
                    stringResource(Res.string.destination_account) else
                        stringResource(Res.string.source_account),
                value = state.accountName,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                errorMessage = stringResource(result.toStringRes()),
                isError = result.isInsufficientAccountBalance()
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