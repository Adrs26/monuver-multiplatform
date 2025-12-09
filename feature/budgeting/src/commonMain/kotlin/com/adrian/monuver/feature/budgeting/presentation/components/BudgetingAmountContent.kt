package com.adrian.monuver.feature.budgeting.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.percentageOf
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.util.calculateProgressBarValue
import com.adrian.monuver.core.presentation.util.changeProgressBarColor
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.already_used
import monuver.feature.budgeting.generated.resources.percentage_value
import monuver.feature.budgeting.generated.resources.your_budgeting_this_period
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetingAmountContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
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
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.your_budgeting_this_period),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = totalMaxAmount.toRupiah(),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
            )
            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = totalUsedAmount.toRupiah(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(Res.string.already_used),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { calculateProgressBarValue(totalUsedAmount, totalMaxAmount) },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { }
                )
                Text(
                    text = stringResource(
                        Res.string.percentage_value,
                        totalUsedAmount.percentageOf(totalMaxAmount)
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}