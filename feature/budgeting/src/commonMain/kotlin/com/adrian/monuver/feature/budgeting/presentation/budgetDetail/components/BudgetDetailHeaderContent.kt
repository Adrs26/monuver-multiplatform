package com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components

import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.feature.budgeting.presentation.components.formatBudgetDate
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.budgeting_time_period
import monuver.feature.budgeting.generated.resources.category
import monuver.feature.budgeting.generated.resources.cycle
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetDetailHeaderContent(
    budget: Budget,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = MaterialTheme.shapes.medium
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BudgetDetailData(
                title = stringResource(Res.string.category),
                content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budget.category))
            )
            BudgetDetailData(
                title = stringResource(Res.string.cycle),
                content = stringResource(DatabaseCodeMapper.toCycle(budget.cycle))
            )
            BudgetDetailData(
                title = stringResource(Res.string.budgeting_time_period),
                content = formatBudgetDate(budget.startDate, budget.endDate)
            )
        }
    }
}

@Composable
private fun BudgetDetailData(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        Text(
            text = ": $content",
            modifier = Modifier.weight(2.5f),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
        )
    }
}