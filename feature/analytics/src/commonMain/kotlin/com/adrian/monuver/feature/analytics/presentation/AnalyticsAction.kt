package com.adrian.monuver.feature.analytics.presentation

internal sealed interface AnalyticsAction {
    data class MonthChange(val month: Int) : AnalyticsAction
    data class YearChange(val year: Int) : AnalyticsAction
    data class TypeChange(val type: Int) : AnalyticsAction
    data class WeekChange(val week: Int) : AnalyticsAction
    data class NavigateToAnalyticsCategoryTransaction(
        val category: Int,
        val month: Int,
        val year: Int
    ) : AnalyticsAction
}