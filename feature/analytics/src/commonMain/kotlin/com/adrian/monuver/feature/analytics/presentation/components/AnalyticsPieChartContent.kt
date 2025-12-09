package com.adrian.monuver.feature.analytics.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.DataProvider
import com.adrian.monuver.core.domain.util.percentageOf
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.theme.SoftWhite
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.analytics.domain.model.TransactionCategorySummary
import kotlinx.coroutines.launch
import monuver.feature.analytics.generated.resources.Res
import monuver.feature.analytics.generated.resources.category_recap
import monuver.feature.analytics.generated.resources.percentage_value
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AnalyticsPieChartContent(
    summaries: List<TransactionCategorySummary>,
    typeFilter: Int,
    monthFilter: Int,
    yearFilter: Int,
    onTypeChange: (Int) -> Unit,
    onNavigateToAnalyticsCategoryTransaction: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.category_recap),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            TypeFilterDropdown(
                typeValue = typeFilter,
                onTypeChange = onTypeChange
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(
                values = summaries
            )
        }
        AnalyticsPieChartDetail(
            summaries = summaries,
            monthValue = monthFilter,
            yearValue = yearFilter,
            onNavigateToAnalyticsCategoryTransaction = onNavigateToAnalyticsCategoryTransaction
        )
    }
}

@Composable
private fun TypeFilterDropdown(
    typeValue: Int,
    onTypeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val typeFilterOptions = DataProvider.provideTransactionTypeFilterOptions().subList(1, 3)

    Box(
        modifier = modifier
    ) {
        AnalyticsFilterDropdown(
            value = stringResource(DatabaseCodeMapper.toTransactionType(typeValue)),
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 150.dp),
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            typeFilterOptions.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onTypeChange(type)
                            expanded = false
                        }
                        .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = if (type == typeValue) MaterialTheme.colorScheme.onSurface else
                            MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = stringResource(DatabaseCodeMapper.toTransactionType(type)),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Composable
internal fun PieChart(
    values: List<TransactionCategorySummary>,
    modifier: Modifier = Modifier
) {
    val numberOfGaps = values.size
    val remainingDegrees = 360 - (numberOfGaps * 12)
    val total = values.fold(0f) { acc, pie -> acc + pie.totalAmount }.div(remainingDegrees)
    var currentSum = 0f

    val arcs = values.mapIndexed { index, pieDataPoint ->
        val startAngle = currentSum + (index * 12)
        currentSum += pieDataPoint.totalAmount.div(total)
        ArcData(
            targetSweepAngle = pieDataPoint.totalAmount.div(total),
            animation = Animatable(0f),
            color = DatabaseCodeMapper.toParentCategoryIconBackground(pieDataPoint.parentCategory),
            startAngle = -90 + startAngle
        )
    }

    LaunchedEffect(key1 = arcs) {
        arcs.mapIndexed { index, arc ->
            launch {
                arc.animation.animateTo(
                    targetValue = arc.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                        delayMillis = index * 150
                    )
                )
            }
        }
    }

    Canvas(
        modifier = modifier.size(160.dp)
    ) {
        val stroke = Stroke(width = 35f, cap = StrokeCap.Round)
        arcs.reversed().map {
            drawArc(
                color = it.color,
                startAngle = it.startAngle,
                sweepAngle = it.animation.value,
                useCenter = false,
                style = stroke
            )
        }
    }
}

@Composable
private fun AnalyticsPieChartDetail(
    summaries: List<TransactionCategorySummary>,
    monthValue: Int,
    yearValue: Int,
    onNavigateToAnalyticsCategoryTransaction: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        summaries.forEach { data ->
            AnalyticsPieChartDetailData(
                category = data.parentCategory,
                amount = data.totalAmount,
                total = summaries.sumOf { it.totalAmount },
                modifier = Modifier
                    .debouncedClickable {
                        onNavigateToAnalyticsCategoryTransaction(
                            data.parentCategory, monthValue, yearValue
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun AnalyticsPieChartDetailData(
    category: Int,
    amount: Long,
    total: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DatabaseCodeMapper.toParentCategoryIconBackground(category)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    Res.string.percentage_value,
                    amount.percentageOf(total)
                ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SoftWhite,
                    fontSize = 12.sp
                )
            )
        }
        Text(
            text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = amount.toRupiah(),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

private data class ArcData(
    val targetSweepAngle: Float,
    val animation: Animatable<Float, AnimationVector1D>,
    val color: Color,
    val startAngle: Float
)