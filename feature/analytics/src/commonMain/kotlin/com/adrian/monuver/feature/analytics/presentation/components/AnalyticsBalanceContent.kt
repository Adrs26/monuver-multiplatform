package com.adrian.monuver.feature.analytics.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.common.ThemeState
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.theme.Green100
import com.adrian.monuver.core.presentation.theme.Green600
import com.adrian.monuver.core.presentation.theme.Red100
import com.adrian.monuver.core.presentation.theme.Red600
import com.adrian.monuver.feature.analytics.domain.model.TransactionBalanceSummary
import monuver.feature.analytics.generated.resources.Res
import monuver.feature.analytics.generated.resources.average_per_day
import monuver.feature.analytics.generated.resources.total_expense
import monuver.feature.analytics.generated.resources.total_income
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AnalyticsBalanceContent(
    summary: TransactionBalanceSummary,
    themeState: ThemeState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalanceSummary(
            title = stringResource(Res.string.total_income),
            containerColor = changeIncomeContainerColor(themeState),
            totalAmount = summary.totalIncome,
            averageAmount = summary.averageIncome,
            modifier = Modifier.weight(1f)
        )
        BalanceSummary(
            title = stringResource(Res.string.total_expense),
            containerColor = changeExpenseContainerColor(themeState),
            totalAmount = summary.totalExpense,
            averageAmount = summary.averageExpense,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BalanceSummary(
    title: String,
    containerColor: Color,
    totalAmount: Long,
    averageAmount: Double,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = totalAmount.toRupiah(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )
            Text(
                text = stringResource(Res.string.average_per_day),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = averageAmount.toLong().toRupiah(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
private fun changeIncomeContainerColor(themeState: ThemeState): Color {
    return when (themeState) {
        ThemeState.Light -> Green100
        ThemeState.Dark -> Green600
        ThemeState.System -> { if (isSystemInDarkTheme()) Green600 else Green100 }
    }
}

@Composable
private fun changeExpenseContainerColor(themeState: ThemeState): Color {
    return when (themeState) {
        ThemeState.Light -> Red100
        ThemeState.Dark -> Red600
        ThemeState.System -> { if (isSystemInDarkTheme()) Red600 else Red100 }
    }
}