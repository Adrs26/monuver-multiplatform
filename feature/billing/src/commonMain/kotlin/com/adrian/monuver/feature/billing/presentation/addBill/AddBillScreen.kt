package com.adrian.monuver.feature.billing.presentation.addBill

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isCreateBillSuccess
import com.adrian.monuver.core.domain.util.isEmptyBillAmount
import com.adrian.monuver.core.domain.util.isEmptyBillDate
import com.adrian.monuver.core.domain.util.isEmptyBillFixPeriod
import com.adrian.monuver.core.domain.util.isEmptyBillTitle
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.CycleFilterField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.components.TextWithSwitch
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.billing.domain.model.AddBill
import com.adrian.monuver.feature.billing.presentation.components.BillPeriodRadioField
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.add
import monuver.feature.billing.generated.resources.add_bill
import monuver.feature.billing.generated.resources.amount
import monuver.feature.billing.generated.resources.choose_due_date
import monuver.feature.billing.generated.resources.due_date
import monuver.feature.billing.generated.resources.enter_bill_title
import monuver.feature.billing.generated.resources.recurring_bill
import monuver.feature.billing.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddBillScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<AddBillViewModel>()
    val result by viewModel.result.collectAsStateWithLifecycle()

    AddBillContent(
        result = result,
        onAction = { action ->
            when (action) {
                is AddBillAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AddBillAction.AddNewBill -> {
                    viewModel.createNewBill(action.bill)
                }
            }
        }
    )
}

@Composable
internal fun AddBillContent(
    result: DatabaseResultState,
    onAction: (AddBillAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val title = rememberTextFieldState(initialText = "")
    val date = rememberTextFieldState(initialText = "")
    val formattedAmount = rememberTextFieldState(initialText = "")

    var rawAmount by remember { mutableLongStateOf(0) }

    var isRecurring by rememberSaveable { mutableStateOf(false) }
    var cycle by rememberSaveable { mutableIntStateOf(Cycle.YEARLY) }
    var period by rememberSaveable { mutableIntStateOf(1) }
    var fixPeriod by rememberSaveable { mutableStateOf("") }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        when {
            result.isCreateBillSuccess() -> {
                onAction(AddBillAction.NavigateBack)
            }
            result.isEmptyBillFixPeriod() -> {
                snackbarHostState.showSnackbar("Periode pembayaran tidak boleh kosong")
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.add_bill),
                onNavigateBack = {
                    onAction(AddBillAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        AddBillAction.AddNewBill(
                            AddBill(
                                title = title.text.toString(),
                                date = date.text.toString(),
                                amount = rawAmount,
                                isRecurring = isRecurring,
                                cycle = cycle,
                                period = period,
                                fixPeriod = fixPeriod
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
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = result.isEmptyBillTitle()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.due_date),
                placeholder = stringResource(Res.string.choose_due_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyBillDate(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.amount),
                errorMessage = stringResource(result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyBillAmount()
            )
            TextWithSwitch(
                text = stringResource(Res.string.recurring_bill),
                checked = isRecurring,
                onCheckedChange = { isRecurring = it },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            AnimatedVisibility(
                visible = isRecurring,
                enter = slideInVertically(initialOffsetY = { -it / 3 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it / 3 }) + fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    CycleFilterField(
                        cycles = listOf(Cycle.YEARLY, Cycle.MONTHLY, Cycle.WEEKLY),
                        selectedCycle = cycle,
                        onCycleChange = { cycle = it },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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