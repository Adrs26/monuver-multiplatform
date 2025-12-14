package com.adrian.monuver.feature.budgeting.presentation.addBudget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isActiveBudgetWithCategoryAlreadyExists
import com.adrian.monuver.core.domain.util.isBudgetEndDateBeforeToday
import com.adrian.monuver.core.domain.util.isBudgetStartDateAfterToday
import com.adrian.monuver.core.domain.util.isCreateBudgetSuccess
import com.adrian.monuver.core.domain.util.isCurrentBudgetAmountExceedsMaximumLimit
import com.adrian.monuver.core.domain.util.isEmptyBudgetCategory
import com.adrian.monuver.core.domain.util.isEmptyBudgetMaxAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonCheckBoxField
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CycleFilterField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.budgeting.domain.model.AddBudget
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.add
import monuver.feature.budgeting.generated.resources.add_budgeting
import monuver.feature.budgeting.generated.resources.budgeting_auto_update
import monuver.feature.budgeting.generated.resources.budgeting_overflow_allowed
import monuver.feature.budgeting.generated.resources.category
import monuver.feature.budgeting.generated.resources.choose_transaction_category
import monuver.feature.budgeting.generated.resources.end_date
import monuver.feature.budgeting.generated.resources.maximum_amount
import monuver.feature.budgeting.generated.resources.start_date
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddBudgetScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<AddBudgetViewModel>(navController)
    val category by viewModel.category.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()

    AddBudgetContent(
        category = category,
        result = result,
        onAction = { action ->
            when (action) {
                is AddBudgetAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AddBudgetAction.NavigateToCategory -> {
                    navController.navigate(Budget.AddCategory)
                }
                is AddBudgetAction.AddNewBudget -> {
                    viewModel.createNewBudgeting(action.budget)
                }
            }
        }
    )
}

@Composable
private fun AddBudgetContent(
    category: Int,
    result: DatabaseResultState,
    onAction: (AddBudgetAction) -> Unit
) {
    val formattedAmount = rememberTextFieldState(initialText = "")
    val startDate = rememberTextFieldState(initialText = DateHelper.getFirstDayOfCurrentMonth())
    val endDate = rememberTextFieldState(initialText = DateHelper.getLastDayOfCurrentMonth())

    var rawAmount by remember { mutableLongStateOf(0) }
    var cycle by rememberSaveable { mutableIntStateOf(Cycle.MONTHLY) }
    var isOverflowAllowed by rememberSaveable { mutableStateOf(false) }
    var isAutoUpdate by rememberSaveable { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result.isCreateBudgetSuccess()) {
            onAction(AddBudgetAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.add_budgeting),
                onNavigateBack = {
                    onAction(AddBudgetAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        AddBudgetAction.AddNewBudget(
                            AddBudget(
                                category = category,
                                maxAmount = rawAmount,
                                cycle = cycle,
                                startDate = getBudgetStartDate(cycle, startDate.text.toString()),
                                endDate = getBudgetEndDate(cycle, endDate.text.toString()),
                                isOverflowAllowed = isOverflowAllowed,
                                isAutoUpdate = isAutoUpdate
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
                value = if (category == 0) "" else
                    stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
                label = stringResource(Res.string.category),
                placeholder = stringResource(Res.string.choose_transaction_category),
                onClick = {
                    onAction(AddBudgetAction.NavigateToCategory)
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = result.isEmptyBudgetCategory() || result.isActiveBudgetWithCategoryAlreadyExists()
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.maximum_amount),
                errorMessage = stringResource(result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyBudgetMaxAmount() || result.isCurrentBudgetAmountExceedsMaximumLimit()
            )
            CycleFilterField(
                cycles = listOf(Cycle.MONTHLY, Cycle.WEEKLY, Cycle.CUSTOM),
                selectedCycle = cycle,
                onCycleChange = {
                    cycle = it
                    startDate.edit {
                        replace(0, length, getBudgetStartDate(it, startDate.text.toString()))
                    }
                    endDate.edit {
                        replace(0, length, getBudgetEndDate(it, endDate.text.toString()))
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
            ClickTextField(
                value = DateHelper.formatToReadable(startDate.text.toString()),
                label = stringResource(Res.string.start_date),
                placeholder = stringResource(Res.string.start_date),
                onClick = {
                    if (cycle == Cycle.CUSTOM) {
                        showDatePickerDialog = true
                        activeField = CalendarField.START
                    }
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isEnabled = cycle == Cycle.CUSTOM,
                isError = result.isBudgetStartDateAfterToday(),
                isDatePicker = true
            )
            ClickTextField(
                value = DateHelper.formatToReadable(endDate.text.toString()),
                label = stringResource(Res.string.end_date),
                placeholder = stringResource(Res.string.end_date),
                onClick = {
                    if (cycle == Cycle.CUSTOM) {
                        showDatePickerDialog = true
                        activeField = CalendarField.END
                    }
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isEnabled = cycle == Cycle.CUSTOM,
                isError = result.isBudgetEndDateBeforeToday(),
                isDatePicker = true
            )
            CommonCheckBoxField(
                checked = isOverflowAllowed,
                onCheckedChange = { isOverflowAllowed = it },
                label = stringResource(Res.string.budgeting_overflow_allowed),
                modifier = Modifier.padding(start = 12.dp, end = 16.dp)
            )
            CommonCheckBoxField(
                checked = isAutoUpdate,
                onCheckedChange = { isAutoUpdate = it },
                label = stringResource(Res.string.budgeting_auto_update),
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, bottom = 16.dp),
                isEnabled = cycle != Cycle.CUSTOM
            )
        }
    }

    if (showDatePickerDialog) {
        CommonDatePicker(
            onDateSelected = { selectedDate ->
                val date = DateHelper.formatToLocalDate(selectedDate ?: 0L)

                when (activeField) {
                    CalendarField.START -> startDate.edit { replace(0, length, date.toString()) }
                    CalendarField.END -> endDate.edit { replace(0, length, date.toString()) }
                    null -> Unit
                }
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}

private fun getBudgetStartDate(budgetCycle: Int, previousDate: String): String {
    return when (budgetCycle) {
        Cycle.MONTHLY -> DateHelper.getFirstDayOfCurrentMonth()
        Cycle.WEEKLY -> DateHelper.getFirstDayOfCurrentWeek()
        else -> previousDate
    }
}

private fun getBudgetEndDate(budgetCycle: Int, previousDate: String): String {
    return when (budgetCycle) {
        Cycle.MONTHLY -> DateHelper.getLastDayOfCurrentMonth()
        Cycle.WEEKLY -> DateHelper.getLastDayOfCurrentWeek()
        else -> previousDate
    }
}

enum class CalendarField { START, END }