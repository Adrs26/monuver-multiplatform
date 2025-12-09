package com.adrian.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.feature.saving.presentation.components.SavingEmptyAnimation
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.transaction_history
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SavingDetailEmptyListContent(
    saving: Saving,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SavingDetailMainOverview(
            title = saving.title,
            targetDate = saving.targetDate,
            isActive = saving.isActive,
            modifier = Modifier.padding(top = 8.dp)
        )
        SavingDetailAmountContent(
            currentAmount = saving.currentAmount,
            targetAmount = saving.targetAmount,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (saving.isActive) {
            SavingDetailButtonContent(
                onNavigateToDeposit = onNavigateToDeposit,
                onNavigateToWithdraw = onNavigateToWithdraw,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Text(
            text = stringResource(Res.string.transaction_history),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        SavingEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    }
}