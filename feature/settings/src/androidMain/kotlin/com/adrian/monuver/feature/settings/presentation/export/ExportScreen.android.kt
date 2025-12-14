package com.adrian.monuver.feature.settings.presentation.export

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.isEmptyReportEndDate
import com.adrian.monuver.core.domain.util.isEmptyReportStartDate
import com.adrian.monuver.core.domain.util.isEmptyReportTitle
import com.adrian.monuver.core.domain.util.isEmptyReportUsername
import com.adrian.monuver.core.domain.util.isReportStartDateAfterEndDate
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonCheckBoxField
import com.adrian.monuver.core.presentation.components.CommonDatePicker
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.settings.domain.common.ExportState
import com.adrian.monuver.feature.settings.domain.model.Export
import com.adrian.monuver.feature.settings.presentation.export.components.ExportProgressDialog
import com.adrian.monuver.feature.settings.presentation.export.components.ExportSortTypeRadioField
import monuver.feature.settings.generated.resources.Res
import monuver.feature.settings.generated.resources.choose_report_end_period
import monuver.feature.settings.generated.resources.choose_report_start_period
import monuver.feature.settings.generated.resources.enter_report_title
import monuver.feature.settings.generated.resources.enter_report_username
import monuver.feature.settings.generated.resources.export
import monuver.feature.settings.generated.resources.export_data
import monuver.feature.settings.generated.resources.group_income_expense
import monuver.feature.settings.generated.resources.include_transfer_transaction
import monuver.feature.settings.generated.resources.report_end_period
import monuver.feature.settings.generated.resources.report_format
import monuver.feature.settings.generated.resources.report_start_period
import monuver.feature.settings.generated.resources.report_title
import monuver.feature.settings.generated.resources.report_username
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal actual fun ExportScreen(navController: NavHostController) {
    val viewModel = koinViewModel<ExportViewModel>()
    val status by viewModel.status.collectAsStateWithLifecycle()

    ExportContent(
        status = status,
        onNavigateBack = navController::navigateUp,
        onValidateData = viewModel::validateData,
        onExportData = viewModel::exportDataToPdf
    )
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ExportContent(
    status: ExportState,
    onNavigateBack: () -> Unit,
    onValidateData: (Export) -> Unit,
    onExportData: (String, Export) -> Unit
) {
    val title = rememberTextFieldState()
    val username = rememberTextFieldState()
    val startDate = rememberTextFieldState()
    val endDate = rememberTextFieldState()

    var sortType by rememberSaveable { mutableIntStateOf(1) }
    var isTransactionGrouped by rememberSaveable { mutableStateOf(false) }
    var isTransferIncluded by rememberSaveable { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showExportProgressDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            if (uri != null) {
                onExportData(
                    uri.toString(),
                    Export(
                        title = title.text.toString(),
                        username = username.text.toString(),
                        startDate = startDate.text.toString(),
                        endDate = endDate.text.toString(),
                        sortType = sortType,
                        isTransactionGrouped = isTransactionGrouped,
                        isTransferIncluded = isTransferIncluded
                    )
                )
            }
        }
    )

    LaunchedEffect(status) {
        when (status) {
            is ExportState.ValidateSuccess -> {
                val timestamp = Clock.System.now().toEpochMilliseconds()
                launcher.launch("monuver_report_$timestamp.pdf")
            }
            is ExportState.Progress -> showExportProgressDialog = true
            is ExportState.Success -> { onNavigateBack() }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.export_data),
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.export),
                onClick = {
                    onValidateData(
                        Export(
                            title = title.text.toString(),
                            username = username.text.toString(),
                            startDate = startDate.text.toString(),
                            endDate = endDate.text.toString(),
                            sortType = sortType,
                            isTransactionGrouped = isTransactionGrouped,
                            isTransferIncluded = isTransferIncluded
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
                label = stringResource(Res.string.report_title),
                placeholder = stringResource(Res.string.enter_report_title),
                errorMessage = status.toErrorMessage(),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = status.isErrorThat { it.error.isEmptyReportTitle() }
            )
            CommonTextField(
                state = username,
                label = stringResource(Res.string.report_username),
                placeholder = stringResource(Res.string.enter_report_username),
                errorMessage = status.toErrorMessage(),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = status.isErrorThat { it.error.isEmptyReportUsername() }
            )
            ClickTextField(
                value = DateHelper.formatToReadable(startDate.text.toString()),
                label = stringResource(Res.string.report_start_period),
                placeholder = stringResource(Res.string.choose_report_start_period),
                onClick = {
                    showDatePickerDialog = true
                    activeField = CalendarField.START
                },
                errorMessage = status.toErrorMessage(),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = status.isErrorThat {
                    it.error.isEmptyReportStartDate() || it.error.isReportStartDateAfterEndDate()
                },
                isDatePicker = true
            )
            ClickTextField(
                value = DateHelper.formatToReadable(endDate.text.toString()),
                label = stringResource(Res.string.report_end_period),
                placeholder = stringResource(Res.string.choose_report_end_period),
                onClick = {
                    showDatePickerDialog = true
                    activeField = CalendarField.END
                },
                errorMessage = status.toErrorMessage(),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = status.isErrorThat { it.error.isEmptyReportEndDate() },
                isDatePicker = true
            )
            DisableTextField(
                label = stringResource(Res.string.report_format),
                value = "PDF",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ExportSortTypeRadioField(
                selectedSortType = sortType,
                onSortTypeSelect = { sortType = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CommonCheckBoxField(
                checked = isTransactionGrouped,
                onCheckedChange = { isTransactionGrouped = it },
                label = stringResource(Res.string.group_income_expense),
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 8.dp)
            )
            CommonCheckBoxField(
                checked = isTransferIncluded,
                onCheckedChange = { isTransferIncluded = it },
                label = stringResource(Res.string.include_transfer_transaction),
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, bottom = 16.dp)
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

    if (showExportProgressDialog) {
        ExportProgressDialog(
            onDismissRequest = { showExportProgressDialog = false }
        )
    }
}

enum class CalendarField { START, END }

@Composable
private fun ExportState.toErrorMessage(): String {
    return if (this is ExportState.Error) {
        stringResource(this.error.toStringRes())
    } else {
        ""
    }
}

private fun ExportState.isErrorThat(check: (ExportState.Error) -> Boolean): Boolean {
    return when (this) {
        is ExportState.Error -> check(this)
        else -> false
    }
}