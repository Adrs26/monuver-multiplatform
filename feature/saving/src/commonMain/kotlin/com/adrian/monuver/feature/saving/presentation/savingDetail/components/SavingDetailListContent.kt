package com.adrian.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.core.presentation.components.TransactionListItem
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.transaction_history
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SavingDetailListContent(
    saving: Saving,
    transactions: List<TransactionItem>,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            SavingDetailMainOverview(
                title = saving.title,
                targetDate = saving.targetDate,
                isActive = saving.isActive,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            SavingDetailAmountContent(
                currentAmount = saving.currentAmount,
                targetAmount = saving.targetAmount,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (saving.isActive) {
            item {
                SavingDetailButtonContent(
                    onNavigateToDeposit = onNavigateToDeposit,
                    onNavigateToWithdraw = onNavigateToWithdraw,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        item {
            Text(
                text = stringResource(Res.string.transaction_history),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium
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
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                isDepositOrWithdraw = true
            )
        }
    }
}