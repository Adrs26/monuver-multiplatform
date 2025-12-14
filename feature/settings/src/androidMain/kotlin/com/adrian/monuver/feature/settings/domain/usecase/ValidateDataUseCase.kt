package com.adrian.monuver.feature.settings.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.feature.settings.domain.common.ExportState
import com.adrian.monuver.feature.settings.domain.model.Export
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ValidateDataUseCase {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(export: Export): ExportState {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = if (export.startDate.isEmpty()) today else LocalDate.parse(export.startDate)
        val endDate = if (export.endDate.isEmpty()) today else LocalDate.parse(export.endDate)

        when {
            export.title.isEmpty() -> {
                return ExportState.Error(DatabaseResultState.EmptyReportTitle)
            }
            export.username.isEmpty() -> {
                return ExportState.Error(DatabaseResultState.EmptyReportUsername)
            }
            export.startDate.isEmpty() -> {
                return ExportState.Error(DatabaseResultState.EmptyReportStartDate)
            }
            export.endDate.isEmpty() -> {
                return ExportState.Error(DatabaseResultState.EmptyReportEndDate)
            }
            startDate > endDate -> {
                return ExportState.Error(DatabaseResultState.ReportStartDateAfterEndDate)
            }
        }

        return ExportState.ValidateSuccess
    }
}