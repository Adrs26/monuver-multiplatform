package com.adrian.monuver.feature.analytics.presentation

import com.adrian.monuver.core.domain.common.ThemeState
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.analytics.domain.model.TransactionBalanceSummary
import com.adrian.monuver.feature.analytics.domain.model.TransactionCategorySummary
import com.adrian.monuver.feature.analytics.domain.model.TransactionDailySummary
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal data class AnalyticsState @OptIn(ExperimentalTime::class) constructor(
    val themeState: ThemeState = ThemeState.System,
    val monthFilter: Int = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date.month.number,
    val yearFilter: Int = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date.year,
    val typeFilter: Int = TransactionType.EXPENSE,
    val weekFilter: Int = DateHelper.getCurrentWeekNumber(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.day
    ),
    val availableYears: List<Int> = emptyList(),
    val balanceSummary: TransactionBalanceSummary = TransactionBalanceSummary(),
    val categorySummaries: List<TransactionCategorySummary> = emptyList(),
    val dailySummaries: List<TransactionDailySummary> = emptyList(),
)
