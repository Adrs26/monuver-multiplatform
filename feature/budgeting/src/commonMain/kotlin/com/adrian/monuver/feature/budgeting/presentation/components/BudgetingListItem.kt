package com.adrian.monuver.feature.budgeting.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.percentageOf
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.calculateProgressBarValue
import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.percentage_value
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetingListItem(
    budget: BudgetItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BudgetingListItemProgressIcon(
            color = DatabaseCodeMapper.toParentCategoryIconBackground(budget.category),
            usedAmount = budget.usedAmount,
            maxAmount = budget.maxAmount
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budget.category)),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = formatBudgetDate(budget.startDate, budget.endDate),
                modifier = Modifier.padding(top = 2.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = budget.usedAmount.toRupiah(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = "/",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = budget.maxAmount.toRupiah(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BudgetingListItemProgressIcon(
    color: Color,
    usedAmount: Long,
    maxAmount: Long
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { calculateProgressBarValue(usedAmount, maxAmount) },
            modifier = Modifier.size(48.dp),
            color = color,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp
        )
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    Res.string.percentage_value,
                    usedAmount.percentageOf(maxAmount)
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

internal fun formatBudgetDate(startDate: String, endDate: String): String {
    return if (startDate == endDate) {
        DateHelper.formatToShortDate(startDate)
    } else {
        "${DateHelper.formatToShortDate(startDate)} - ${DateHelper.formatToShortDate(endDate)}"
    }
}