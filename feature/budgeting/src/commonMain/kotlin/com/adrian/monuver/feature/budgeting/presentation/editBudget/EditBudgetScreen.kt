package com.adrian.monuver.feature.budgeting.presentation.editBudget

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
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isBudgetEndDateBeforeToday
import com.adrian.monuver.core.domain.util.isBudgetStartDateAfterToday
import com.adrian.monuver.core.domain.util.isCurrentBudgetAmountExceedsMaximumLimit
import com.adrian.monuver.core.domain.util.isEmptyBudgetMaxAmount
import com.adrian.monuver.core.domain.util.isUpdateBudgetSuccess
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonCheckBoxField
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.budgeting.domain.model.EditBudget
import com.adrian.monuver.feature.budgeting.presentation.addBudget.CalendarField
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.budgeting_auto_update
import monuver.feature.budgeting.generated.resources.budgeting_overflow_allowed
import monuver.feature.budgeting.generated.resources.category
import monuver.feature.budgeting.generated.resources.edit_budget
import monuver.feature.budgeting.generated.resources.end_date
import monuver.feature.budgeting.generated.resources.maximum_amount
import monuver.feature.budgeting.generated.resources.save
import monuver.feature.budgeting.generated.resources.start_date
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditBudgetScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<EditBudgetViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { state ->
        EditBudgetContent(
            state = state,
            onAction = { action ->
                when (action) {
                    is EditBudgetAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditBudgetAction.EditCurrentBudget -> {
                        viewModel.updateBudget(action.budget)
                    }
                }
            }
        )
    }
}

@Composable
internal fun EditBudgetContent(
    state: EditBudgetState,
    onAction: (EditBudgetAction) -> Unit,
) {
    val formattedAmount = rememberTextFieldState(initialText = state.maxAmount.toFormattedAmount())
    val startDate = rememberTextFieldState(initialText = state.startDate)
    val endDate = rememberTextFieldState(initialText = state.endDate)

    var rawAmount by remember { mutableLongStateOf(state.maxAmount) }
    var isOverflowAllowed by rememberSaveable { mutableStateOf(state.isOverflowAllowed) }
    var isAutoUpdate by rememberSaveable { mutableStateOf(state.isAutoUpdate) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isUpdateBudgetSuccess()) {
            onAction(EditBudgetAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.edit_budget),
                onNavigateBack = {
                    onAction(EditBudgetAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.save),
                onClick = {
                    onAction(
                        EditBudgetAction.EditCurrentBudget(
                            EditBudget(
                                id = state.id,
                                category = state.category,
                                maxAmount = rawAmount,
                                cycle = state.cycle,
                                startDate = startDate.text.toString(),
                                endDate = endDate.text.toString(),
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
            DisableTextField(
                label = stringResource(Res.string.category),
                value = stringResource(DatabaseCodeMapper.toParentCategoryTitle(state.category)),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.maximum_amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptyBudgetMaxAmount() || state.result.isCurrentBudgetAmountExceedsMaximumLimit()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(startDate.text.toString()),
                label = stringResource(Res.string.start_date),
                placeholder = stringResource(Res.string.start_date),
                onClick = {
                    if (state.cycle == Cycle.CUSTOM) {
                        showDatePickerDialog = true
                        activeField = CalendarField.START
                    }
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isEnabled = state.cycle == Cycle.CUSTOM,
                isError = state.result.isBudgetStartDateAfterToday(),
                isDatePicker = true
            )
            ClickTextField(
                value = DateHelper.formatToReadable(endDate.text.toString()),
                label = stringResource(Res.string.end_date),
                placeholder = stringResource(Res.string.end_date),
                onClick = {
                    if (state.cycle == Cycle.CUSTOM) {
                        showDatePickerDialog = true
                        activeField = CalendarField.END
                    }
                },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isEnabled = state.cycle == Cycle.CUSTOM,
                isError = state.result.isBudgetEndDateBeforeToday(),
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
                isEnabled = state.cycle != Cycle.CUSTOM
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