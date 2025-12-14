package com.adrian.monuver.feature.settings.domain.manager

import com.adrian.monuver.feature.settings.domain.model.Report

internal interface ExportManager {
    suspend fun exportToPdf(stringUri: String, report: Report)
}