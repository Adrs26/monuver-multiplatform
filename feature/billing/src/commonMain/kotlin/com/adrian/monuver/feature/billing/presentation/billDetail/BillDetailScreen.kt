package com.adrian.monuver.feature.billing.presentation.billDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Bill
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isCancelBillSuccess
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.components.DetailDataContent
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.feature.billing.presentation.billDetail.components.BillDetailAppBar
import com.adrian.monuver.feature.billing.presentation.billDetail.components.BillDetailBottomBar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.amount
import monuver.feature.billing.generated.resources.bill_period
import monuver.feature.billing.generated.resources.cycle
import monuver.feature.billing.generated.resources.delete_this_bill
import monuver.feature.billing.generated.resources.due_date
import monuver.feature.billing.generated.resources.fix_period_times
import monuver.feature.billing.generated.resources.one_time_bill
import monuver.feature.billing.generated.resources.overdue
import monuver.feature.billing.generated.resources.paid
import monuver.feature.billing.generated.resources.paid_on
import monuver.feature.billing.generated.resources.payment_number
import monuver.feature.billing.generated.resources.recurring_bill
import monuver.feature.billing.generated.resources.status
import monuver.feature.billing.generated.resources.title
import monuver.feature.billing.generated.resources.type
import monuver.feature.billing.generated.resources.unlimited
import monuver.feature.billing.generated.resources.waiting_for_pay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun BillDetailScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<BillDetailViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val bill by viewModel.bill.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()

    bill?.let { bill ->
        BillDetailContent(
            bill = bill,
            result = result,
            onAction = { action ->
                when (action) {
                    is BillDetailAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is BillDetailAction.NavigateToEditBill -> {
                        navController.navigate(Billing.Edit(action.billId))
                    }
                    is BillDetailAction.NavigateToPayBill -> {
                        navController.navigate(Billing.Payment(action.billId))
                    }
                    else -> viewModel.onAction(action)
                }
            }
        )
    }
}

@Composable
private fun BillDetailContent(
    bill: Bill,
    result: DatabaseResultState,
    onAction: (BillDetailAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var showRemoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result.isCancelBillSuccess()) {
            snackbarHostState.showSnackbar("Pembayaran tagihan telah dibatalkan")
        }
    }

    Scaffold(
        topBar = {
            BillDetailAppBar(
                isPaid = bill.isPaid,
                onNavigateBack = {
                    onAction(BillDetailAction.NavigateBack)
                },
                onNavigateToEditBill = {
                    onAction(BillDetailAction.NavigateToEditBill(bill.id))
                },
                onRemoveBill = { showRemoveDialog = true }
            )
        },
        bottomBar = {
            BillDetailBottomBar(
                isPaid = bill.isPaid,
                onProcessPayment = {
                    onAction(BillDetailAction.NavigateToPayBill(bill.id))
                },
                onCancelPayment = {
                    onAction(BillDetailAction.CancelBillPayment(bill.id))
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailDataContent(
                title = stringResource(Res.string.title),
                content = bill.title,
                modifier = Modifier.padding(top = 32.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.due_date),
                content = DateHelper.formatToReadable(bill.dueDate)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.amount),
                content = bill.amount.toRupiah()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.type),
                content = if (bill.isRecurring) stringResource(Res.string.recurring_bill) else
                    stringResource(Res.string.one_time_bill)
            )
            if (bill.isRecurring) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailDataContent(
                    title = stringResource(Res.string.cycle),
                    content = stringResource(DatabaseCodeMapper.toCycle(bill.cycle ?: 0))
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailDataContent(
                    title = stringResource(Res.string.bill_period),
                    content = if (bill.period == 1) stringResource(Res.string.unlimited) else
                        stringResource(Res.string.fix_period_times, bill.fixPeriod ?: 0)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailDataContent(
                    title = stringResource(Res.string.payment_number),
                    content = bill.nowPaidPeriod.toString()
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.status),
                content = getStatusInformationText(bill.dueDate, bill.isPaid)
            )
            if (bill.isPaid) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailDataContent(
                    title = stringResource(Res.string.paid_on),
                    content = DateHelper.formatToReadable(bill.paidDate ?: "2025-08-21"),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.delete_this_bill),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                onAction(BillDetailAction.RemoveBill(bill.id))
                onAction(BillDetailAction.NavigateBack)
            }
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getStatusInformationText(dueDate: String, isPaid: Boolean): String {
    return if (isPaid) {
        stringResource(Res.string.paid)
    } else {
        val date = LocalDate.parse(dueDate)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (date < today || date == today) {
            stringResource(Res.string.overdue)
        } else {
            stringResource(Res.string.waiting_for_pay)
        }
    }
}