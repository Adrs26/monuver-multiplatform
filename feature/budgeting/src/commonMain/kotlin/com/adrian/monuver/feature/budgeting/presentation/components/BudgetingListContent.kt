package com.adrian.monuver.feature.budgeting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.list_active_budgeting
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetingListContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<BudgetItem>,
    onNavigateToBudgetDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        item {
            BudgetingAmountContent(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(Res.string.list_active_budgeting),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = budgets.size,
            key = { index -> budgets[index].id }
        ) { index ->
            BudgetingListItem(
                budget = budgets[index],
                modifier = Modifier
                    .debouncedClickable { onNavigateToBudgetDetail(budgets[index].id) }
            )
        }
    }
}