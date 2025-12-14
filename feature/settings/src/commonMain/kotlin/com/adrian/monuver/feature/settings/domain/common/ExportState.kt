package com.adrian.monuver.feature.settings.domain.common

import com.adrian.monuver.core.domain.common.DatabaseResultState

internal sealed class ExportState {
    data object Idle: ExportState()
    data object ValidateSuccess : ExportState()
    data object Progress : ExportState()
    data object Success : ExportState()
    data class Error(val error: DatabaseResultState) : ExportState()
}