package com.adrian.monuver.feature.settings.domain.manager

import com.adrian.monuver.feature.settings.domain.model.Report

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual interface ExportManager {
    actual suspend fun exportToPdf(report: Report)
}