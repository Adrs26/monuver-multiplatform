package com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.core.presentation.components.TransactionListItem
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.transaction_history
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetDetailListContent(
    budget: Budget,
    transactions: List<TransactionItem>,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            BudgetDetailHeaderContent(
                budget = budget,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            BudgetDetailAmountContent(
                budget = budget,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(Res.string.transaction_history),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = transactions.size,
            key = { index -> transactions[index].id }
        ) { index ->
            TransactionListItem(
                transaction = transactions[index],
                modifier = Modifier
                    .debouncedClickable { onNavigateToTransactionDetail(transactions[index].id) }
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}