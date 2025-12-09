package com.adrian.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.presentation.theme.Red600
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.day_after_target
import monuver.feature.saving.generated.resources.day_before_target
import monuver.feature.saving.generated.resources.save_target_date_information
import monuver.feature.saving.generated.resources.today_is_target
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun SavingDetailMainOverview(
    title: String,
    targetDate: String,
    isActive: Boolean,
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
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            if (isActive) {
                Text(
                    text = stringResource(
                        Res.string.save_target_date_information,
                        DateHelper.formatToReadable(targetDate),
                        getDayDifference(targetDate)
                    ),
                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = getDayDifferenceColor(targetDate)
                    )
                )
            } else {
                Text(
                    text = DateHelper.formatToReadable(targetDate),
                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getDayDifference(inputDate: String): String {
    val parsedInput = LocalDate.parse(inputDate)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val dayDifference = today.daysUntil(parsedInput)

    return when {
        dayDifference > 0 -> stringResource(Res.string.day_before_target, dayDifference)
        dayDifference < 0 -> stringResource(Res.string.day_after_target, -dayDifference)
        else -> stringResource(Res.string.today_is_target)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getDayDifferenceColor(inputDate: String): Color {
    val parsedInput = LocalDate.parse(inputDate)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val dayDifference = today.daysUntil(parsedInput)

    return when {
        dayDifference < 0 -> Red600
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}