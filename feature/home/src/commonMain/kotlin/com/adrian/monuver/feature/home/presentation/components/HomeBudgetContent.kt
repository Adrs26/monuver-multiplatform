package com.adrian.monuver.feature.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.percentageOf
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.util.calculateProgressBarValue
import com.adrian.monuver.core.presentation.util.changeProgressBarColor
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.home.generated.resources.Res
import monuver.feature.home.generated.resources.budgeting_recap
import monuver.feature.home.generated.resources.maximum_amount
import monuver.feature.home.generated.resources.percentage_value
import monuver.feature.home.generated.resources.remained_amount
import monuver.feature.home.generated.resources.see_budgeting
import monuver.feature.home.generated.resources.used_amount
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeBudgetContent(
    totalUsedAmount: Long,
    totalMaxAmount: Long,
    onNavigateToBudgeting: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.budgeting_recap),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(Res.string.see_budgeting),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .debouncedClickable { onNavigateToBudgeting() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { calculateProgressBarValue(totalUsedAmount, totalMaxAmount) },
                    modifier = Modifier.size(128.dp),
                    color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = stringResource(
                        Res.string.percentage_value,
                        totalUsedAmount.percentageOf(totalMaxAmount)
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                        fontSize = 20.sp
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(Res.string.maximum_amount),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = totalMaxAmount.toRupiah(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.used_amount),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = totalUsedAmount.toRupiah(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.remained_amount),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (totalUsedAmount >= totalMaxAmount) 0L.toRupiah() else
                        (totalMaxAmount - totalUsedAmount).toRupiah(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp)
                )
            }
        }
    }
}