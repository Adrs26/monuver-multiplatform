package com.adrian.monuver.feature.transaction.presentation.addTransaction

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.domain.util.isCurrentBudgetAmountExceedsMaximumLimit
import com.adrian.monuver.core.domain.util.isEmptyTransactionAmount
import com.adrian.monuver.core.domain.util.isEmptyTransactionCategory
import com.adrian.monuver.core.domain.util.isEmptyTransactionDate
import com.adrian.monuver.core.domain.util.isEmptyTransactionDestination
import com.adrian.monuver.core.domain.util.isEmptyTransactionSource
import com.adrian.monuver.core.domain.util.isEmptyTransactionTitle
import com.adrian.monuver.core.domain.util.isInsufficientAccountBalance
import com.adrian.monuver.core.domain.util.isTransactionDateAfterToday
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.components.ReactiveTextField
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.transaction.domain.model.AddTransaction
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monuver.feature.transaction.generated.resources.Res
import monuver.feature.transaction.generated.resources.add
import monuver.feature.transaction.generated.resources.add_expense
import monuver.feature.transaction.generated.resources.add_income
import monuver.feature.transaction.generated.resources.amount
import monuver.feature.transaction.generated.resources.category
import monuver.feature.transaction.generated.resources.choose_destination_account
import monuver.feature.transaction.generated.resources.choose_source_account
import monuver.feature.transaction.generated.resources.choose_transaction_category
import monuver.feature.transaction.generated.resources.choose_transaction_date
import monuver.feature.transaction.generated.resources.date
import monuver.feature.transaction.generated.resources.destination_account
import monuver.feature.transaction.generated.resources.enter_transaction_title
import monuver.feature.transaction.generated.resources.source_account
import monuver.feature.transaction.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun AddTransactionScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<AddTransactionViewModel>(navController)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val type = navBackStackEntry.toRoute<Transaction.Add>().type

    AddTransactionContent(
        type = type,
        state = state,
        onAction = { action ->
            when (action) {
                is AddTransactionAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AddTransactionAction.NavigateToCategory -> {
                    navController.navigate(Transaction.AddCategory(action.transactionType))
                }
                is AddTransactionAction.NavigateToSource -> {
                    navController.navigate(Transaction.AddAccount)
                }
                is AddTransactionAction.ShowWarningAlert -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("warning_condition", action.warning)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("budget_category", action.category)
                }
                is AddTransactionAction.TitleChange -> {
                    viewModel.setTransactionTitle(action.title)
                }
                is AddTransactionAction.AddNewTransaction -> {
                    viewModel.createTransaction(action.transaction)
                }
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
@Composable
internal fun AddTransactionContent(
    type: Int,
    state: AddTransactionState,
    onAction: (AddTransactionAction) -> Unit
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val date = rememberTextFieldState(initialText = today.toString())
    val formattedAmount = rememberTextFieldState(initialText = "")

    var rawAmount by rememberSaveable { mutableLongStateOf(0) }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.result) {
        when (state.result) {
            is DatabaseResultState.CreateSuccessWithWarningCondition -> {
                onAction(
                    AddTransactionAction.ShowWarningAlert(
                        warning = state.result.warningCondition,
                        category = state.result.category
                    )
                )
                onAction(AddTransactionAction.NavigateBack)
            }
            is DatabaseResultState.CreateTransactionSuccess -> {
                onAction(AddTransactionAction.NavigateBack)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (type == TransactionType.INCOME)
                    stringResource(Res.string.add_income) else
                        stringResource(Res.string.add_expense),
                onNavigateBack = {
                    onAction(AddTransactionAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        AddTransactionAction.AddNewTransaction(
                            AddTransaction(
                                title = state.title,
                                type = type,
                                parentCategory = state.parentCategory,
                                childCategory = state.childCategory,
                                date = date.text.toString(),
                                amount = rawAmount,
                                accountId = state.accountId,
                                accountName = state.accountName
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
            ReactiveTextField(
                value = state.title,
                onValueChange = { title ->
                    onAction(AddTransactionAction.TitleChange(title))
                },
                label = stringResource(Res.string.title),
                placeholder = stringResource(Res.string.enter_transaction_title),
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = state.result.isEmptyTransactionTitle()
            )
            ClickTextField(
                value = if (state.childCategory == 0) "" else
                    stringResource(DatabaseCodeMapper.toChildCategoryTitle(state.childCategory)),
                label = stringResource(Res.string.category),
                placeholder = stringResource(Res.string.choose_transaction_category),
                onClick = {
                    onAction(AddTransactionAction.NavigateToCategory(type))
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyTransactionCategory() || state.result.isCurrentBudgetAmountExceedsMaximumLimit()
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
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyTransactionAmount()
            )
            ClickTextField(
                value = state.accountName,
                label = if (type == TransactionType.INCOME)
                    stringResource(Res.string.destination_account) else
                        stringResource(Res.string.source_account),
                placeholder = if (type == TransactionType.INCOME)
                    stringResource(Res.string.choose_destination_account) else
                        stringResource(Res.string.choose_source_account),
                onClick = {
                    onAction(AddTransactionAction.NavigateToSource)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = (if (type == TransactionType.INCOME)
                    state.result.isEmptyTransactionDestination() else
                        state.result.isEmptyTransactionSource()) ||
                        state.result.isInsufficientAccountBalance(),
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