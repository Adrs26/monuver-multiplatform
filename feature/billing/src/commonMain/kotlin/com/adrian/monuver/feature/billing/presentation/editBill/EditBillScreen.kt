package com.adrian.monuver.feature.billing.presentation.editBill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isEmptyBillAmount
import com.adrian.monuver.core.domain.util.isEmptyBillDate
import com.adrian.monuver.core.domain.util.isEmptyBillFixPeriod
import com.adrian.monuver.core.domain.util.isEmptyBillTitle
import com.adrian.monuver.core.domain.util.isInvalidBillFixPeriod
import com.adrian.monuver.core.domain.util.isUpdateBillSuccess
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.billing.domain.model.EditBill
import com.adrian.monuver.feature.billing.presentation.components.BillPeriodRadioField
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.amount
import monuver.feature.billing.generated.resources.choose_due_date
import monuver.feature.billing.generated.resources.due_date
import monuver.feature.billing.generated.resources.edit_bill
import monuver.feature.billing.generated.resources.enter_bill_title
import monuver.feature.billing.generated.resources.save
import monuver.feature.billing.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditBillScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<EditBillViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { state ->
        EditBillContent(
            state = state,
            onAction = { action ->
                when (action) {
                    is EditBillAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditBillAction.EditCurrentBill -> {
                        viewModel.updateBill(action.bill)
                    }
                }
            }
        )
    }
}

@Composable
internal fun EditBillContent(
    state: EditBillState,
    onAction: (EditBillAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val title = rememberTextFieldState(initialText = state.title)
    val date = rememberTextFieldState(initialText = state.date)
    val formattedAmount = rememberTextFieldState(initialText = state.amount.toFormattedAmount())

    var rawAmount by remember { mutableLongStateOf(state.amount) }

    var period by rememberSaveable { mutableIntStateOf(state.period) }
    var fixPeriod by rememberSaveable { mutableStateOf(state.fixPeriod) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        when {
            state.result.isUpdateBillSuccess() -> {
                onAction(EditBillAction.NavigateBack)
            }
            state.result.isEmptyBillFixPeriod() -> {
                snackbarHostState.showSnackbar("Periode pembayaran tidak boleh kosong")
            }
            state.result.isInvalidBillFixPeriod() -> {
                snackbarHostState.showSnackbar("Periode pembayaran tidak valid")
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.edit_bill),
                onNavigateBack = {
                    onAction(EditBillAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.save),
                onClick = {
                    onAction(
                        EditBillAction.EditCurrentBill(
                            EditBill(
                                id = state.id,
                                parentId = state.parentId,
                                title = title.text.toString(),
                                date = date.text.toString(),
                                amount = rawAmount,
                                timeStamp = state.timeStamp,
                                isRecurring = state.isRecurring,
                                cycle = state.cycle,
                                period = period,
                                fixPeriod = fixPeriod,
                                nowPaidPeriod = state.nowPaidPeriod,
                                isPaidBefore = state.isPaidBefore
                            )
                        )
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
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
                placeholder = stringResource(Res.string.enter_bill_title),
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = state.result.isEmptyBillTitle()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.due_date),
                placeholder = stringResource(Res.string.choose_due_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyBillDate(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyBillAmount()
            )
            if (state.isRecurring && !state.isPaidBefore) {
                BillPeriodRadioField(
                    selectedPeriod = period,
                    onPeriodSelect = {
                        period = it
                        if (period == 1) fixPeriod = ""
                    },
                    fixPeriod = fixPeriod,
                    onFixPeriodChange = { fixPeriod = it },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
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