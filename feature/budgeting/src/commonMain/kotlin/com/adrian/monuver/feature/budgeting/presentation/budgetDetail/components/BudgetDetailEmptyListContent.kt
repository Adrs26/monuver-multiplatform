package com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingEmptyAnimation
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.transaction_history
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetDetailEmptyListContent(
    budget: Budget,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BudgetDetailHeaderContent(
            budget = budget,
            modifier = Modifier.padding(top = 8.dp)
        )
        BudgetDetailAmountContent(
            budget = budget,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(Res.string.transaction_history),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        BudgetingEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    }
}