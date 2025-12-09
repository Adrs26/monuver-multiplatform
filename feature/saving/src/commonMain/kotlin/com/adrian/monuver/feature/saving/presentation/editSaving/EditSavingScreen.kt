package com.adrian.monuver.feature.saving.presentation.editSaving

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
import com.adrian.monuver.core.domain.util.isEmptySavingTargetAmount
import com.adrian.monuver.core.domain.util.isEmptySavingTargetDate
import com.adrian.monuver.core.domain.util.isEmptySavingTitle
import com.adrian.monuver.core.domain.util.isSavingTargetDateBeforeToday
import com.adrian.monuver.core.domain.util.isUpdateSavingSuccess
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.saving.domain.model.EditSaving
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.choose_transaction_date
import monuver.feature.saving.generated.resources.edit_save
import monuver.feature.saving.generated.resources.enter_save_title
import monuver.feature.saving.generated.resources.save
import monuver.feature.saving.generated.resources.target_amount
import monuver.feature.saving.generated.resources.target_date
import monuver.feature.saving.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditSavingScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<EditSavingViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { state ->
        EditSavingContent(
            state = state,
            onAction = { action ->
                when (action) {
                    is EditSavingAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditSavingAction.EditCurrentSaving -> {
                        viewModel.updateSaving(action.saving)
                    }
                }
            }
        )
    }
}

@Composable
private fun EditSavingContent(
    state: EditSavingState,
    onAction: (EditSavingAction) -> Unit
) {
    val title = rememberTextFieldState(initialText = state.title)
    val date = rememberTextFieldState(initialText = state.targetDate)
    val formattedAmount = rememberTextFieldState(initialText = state.targetAmount.toFormattedAmount())

    var rawAmount by remember { mutableLongStateOf(state.targetAmount) }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.result) {
        if (state.result.isUpdateSavingSuccess()) {
            onAction(EditSavingAction.NavigateBack)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.edit_save),
                onNavigateBack = {
                    onAction(EditSavingAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.save),
                onClick = {
                    onAction(
                        EditSavingAction.EditCurrentSaving(
                            EditSaving(
                                id = state.id,
                                title = title.text.toString(),
                                targetDate = date.text.toString(),
                                targetAmount = rawAmount,
                                currentAmount = state.currentAmount
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
                placeholder = stringResource(Res.string.enter_save_title),
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = state.result.isEmptySavingTitle()
            )
            ClickTextField(
                value = DateHelper.formatToReadable(date.text.toString()),
                label = stringResource(Res.string.target_date),
                placeholder = stringResource(Res.string.choose_transaction_date),
                onClick = { showDatePickerDialog = true },
                errorMessage = stringResource(state.result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = state.result.isEmptySavingTargetDate() || state.result.isSavingTargetDateBeforeToday(),
                isDatePicker = true
            )
            NumberTextField(
                state = formattedAmount,
                label = stringResource(Res.string.target_amount),
                errorMessage = stringResource(state.result.toStringRes()),
                onValueAsLongCent = { rawAmount = it },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isError = state.result.isEmptySavingTargetAmount()
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