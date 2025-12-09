package com.adrian.monuver.feature.analytics.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Analytics
import com.adrian.monuver.feature.analytics.presentation.components.AnalyticsAppBar
import com.adrian.monuver.feature.analytics.presentation.components.AnalyticsBalanceContent
import com.adrian.monuver.feature.analytics.presentation.components.AnalyticsBarChartContent
import com.adrian.monuver.feature.analytics.presentation.components.AnalyticsPieChartContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<AnalyticsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AnalyticsContent(
        state = state,
        onAction = { action ->
            when(action) {
                is AnalyticsAction.MonthChange -> {
                    viewModel.setMonthFilter(action.month)
                }
                is AnalyticsAction.YearChange -> {
                    viewModel.setYearFilter(action.year)
                }
                is AnalyticsAction.TypeChange -> {
                    viewModel.setTypeFilter(action.type)
                }
                is AnalyticsAction.WeekChange -> {
                    viewModel.setWeekFilter(action.week)
                }
                is AnalyticsAction.NavigateToAnalyticsCategoryTransaction -> {
                    navController.navigate(
                        Analytics.Transaction(
                            category = action.category,
                            month = action.month,
                            year = action.year
                        )
                    )
                }
            }
        }
    )
}

@Composable
private fun AnalyticsContent(
    state: AnalyticsState,
    onAction: (AnalyticsAction) -> Unit
) {
    Scaffold(
        topBar = {
            AnalyticsAppBar(
                monthValue = state.monthFilter,
                yearValue = state.yearFilter,
                yearFilterOptions = state.availableYears,
                onMonthChange = { month ->
                    onAction(AnalyticsAction.MonthChange(month))
                },
                onYearChange = { year ->
                    onAction(AnalyticsAction.YearChange(year))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AnalyticsBalanceContent(
                summary = state.balanceSummary,
                themeState = state.themeState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            AnalyticsBarChartContent(
                summaries = state.dailySummaries,
                monthFilter = state.monthFilter,
                yearFilter = state.yearFilter,
                weekFilter = state.weekFilter,
                onWeekChange = { week ->
                    onAction(AnalyticsAction.WeekChange(week))
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnalyticsPieChartContent(
                summaries = state.categorySummaries,
                typeFilter = state.typeFilter,
                monthFilter = state.monthFilter,
                yearFilter = state.yearFilter,
                onTypeChange = { type ->
                    onAction(AnalyticsAction.TypeChange(type))
                },
                onNavigateToAnalyticsCategoryTransaction = { category, month, year ->
                    onAction(
                        AnalyticsAction.NavigateToAnalyticsCategoryTransaction(
                            category = category,
                            month = month,
                            year = year
                        )
                    )
                },
                modifier = Modifier.padding(bottom = 24.dp),
            )
        }
    }
}