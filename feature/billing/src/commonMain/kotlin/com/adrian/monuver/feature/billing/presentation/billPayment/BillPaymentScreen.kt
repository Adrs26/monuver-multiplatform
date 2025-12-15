package com.adrian.monuver.feature.billing.presentation.billPayment

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isCurrentBudgetAmountExceedsMaximumLimit
import com.adrian.monuver.core.domain.util.isEmptyTransactionCategory
import com.adrian.monuver.core.domain.util.isEmptyTransactionDate
import com.adrian.monuver.core.domain.util.isEmptyTransactionSource
import com.adrian.monuver.core.domain.util.isEmptyTransactionTitle
import com.adrian.monuver.core.domain.util.isInsufficientAccountBalance
import com.adrian.monuver.core.domain.util.isPayBillSuccess
import com.adrian.monuver.core.domain.util.isTransactionDateAfterToday
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.components.ReactiveTextField
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.billing.domain.model.BillPayment
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.amount
import monuver.feature.billing.generated.resources.category
import monuver.feature.billing.generated.resources.choose_source_account
import monuver.feature.billing.generated.resources.choose_transaction_category
import monuver.feature.billing.generated.resources.choose_transaction_date
import monuver.feature.billing.generated.resources.date
import monuver.feature.billing.generated.resources.enter_transaction_title
import monuver.feature.billing.generated.resources.pay
import monuver.feature.billing.generated.resources.pay_bill
import monuver.feature.billing.generated.resources.source_account
import monuver.feature.billing.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun BillPaymentScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<BillPaymentViewModel>(navController) {
        parametersOf(navBackStackEntry.savedStateHandle)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.bill?.let { bill ->
        BillPaymentContent(
            state = state,
            onAction = { action ->
                when (action) {
                    is BillPaymentAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is BillPaymentAction.NavigateToCategory -> {
                        navController.navigate(Billing.PaymentCategory)
                    }
                    is BillPaymentAction.NavigateToSource -> {
                        navController.navigate(Billing.PaymentAccount)
                    }
                    is BillPaymentAction.TitleChange -> {
                        viewModel.setTransactionTitle(action.title)
                    }
                    is BillPaymentAction.PayCurrentBill -> {
                        viewModel.processBillPayment(bill, action.bill)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun BillPaymentContent(
    state: BillPaymentState,
    onAction: (BillPaymentAction) -> Unit
) {
    val date = rememberTextFieldState(
        initialText = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
    )

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isPayBillSuccess()) {
            onAction(BillPaymentAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.pay_bill),
                onNavigateBack = {
                    onAction(BillPaymentAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.pay),
                onClick = {
                    onAction(
                        BillPaymentAction.PayCurrentBill(
                            BillPayment(
                                title = state.title,
                                parentCategory = state.parentCategory,
                                childCategory = state.childCategory,
                                date = date.text.toString(),
                                amount = state.bill?.amount ?: 0,
                                sourceId = state.accountId,
                                sourceName = state.accountName
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
                    onAction(BillPaymentAction.TitleChange(title))
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
                    onAction(BillPaymentAction.NavigateToCategory)
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
            DisableTextField(
                label = stringResource(Res.string.amount),
                value = state.bill?.amount?.toFormattedAmount() ?: 0L.toFormattedAmount(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ClickTextField(
                value = state.accountName,
                label = stringResource(Res.string.source_account),
                placeholder = stringResource(Res.string.choose_source_account),
                onClick = {
                    onAction(BillPaymentAction.NavigateToSource)
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = state.result.isEmptyTransactionSource() || state.result.isInsufficientAccountBalance(),
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