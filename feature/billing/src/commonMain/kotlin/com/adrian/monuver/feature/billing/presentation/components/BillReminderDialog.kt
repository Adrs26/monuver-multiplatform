package com.adrian.monuver.feature.billing.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adrian.monuver.core.presentation.components.CommonCheckBoxField
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.advance_reminder_settings
import monuver.feature.billing.generated.resources.apply
import monuver.feature.billing.generated.resources.bill
import monuver.feature.billing.generated.resources.cancel
import monuver.feature.billing.generated.resources.days_before
import monuver.feature.billing.generated.resources.due_day_reminder
import monuver.feature.billing.generated.resources.five_days_before
import monuver.feature.billing.generated.resources.remind_everyday_after_due_day
import monuver.feature.billing.generated.resources.remind_everyday_for_due_bill
import monuver.feature.billing.generated.resources.three_days_before
import monuver.feature.billing.generated.resources.weeks_before
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BillReminderDialog(
    reminderDaysBeforeDue: Int,
    isReminderBeforeDueDayEnabled: Boolean,
    isReminderForDueBillEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onSettingsApply: (ReminderState) -> Unit,
    modifier: Modifier = Modifier
) {
    var tempDueDayReminder by remember { mutableIntStateOf(reminderDaysBeforeDue) }
    var tempReminderBeforeDueDay by remember { mutableStateOf(isReminderBeforeDueDayEnabled) }
    var tempReminderForDueBill by remember { mutableStateOf(isReminderForDueBillEnabled) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                DueDayReminderSettings(
                    dueDayReminder = tempDueDayReminder,
                    onDueDayReminderChange = { tempDueDayReminder = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                AdvanceReminderSettings(
                    isReminderAfterDueDayEnabled = tempReminderBeforeDueDay,
                    isReminderForDueBillEnabled = tempReminderForDueBill,
                    onReminderAfterDueDayEnableChange = { tempReminderBeforeDueDay = it },
                    onReminderForDueBillEnableChange = { tempReminderForDueBill = it }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Button(
                        onClick = {
                            onSettingsApply(
                                ReminderState(
                                    dueDayReminder = tempDueDayReminder,
                                    isReminderBeforeDueDayEnabled = tempReminderBeforeDueDay,
                                    isReminderForDueBillEnabled = tempReminderForDueBill
                                )
                            )
                            onDismissRequest()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(Res.string.apply),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DueDayReminderSettings(
    dueDayReminder: Int,
    onDueDayReminderChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.due_day_reminder),
            style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        listOf(1, 3, 5, 7).forEach { dueDay ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDueDayReminderChange(dueDay) }
            ) {
                RadioButton(
                    selected = dueDayReminder == dueDay,
                    onClick = { onDueDayReminderChange(dueDay) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(dueDay.toDayReminderStringResource()),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
            }
        }
    }
}

@Composable
private fun AdvanceReminderSettings(
    isReminderAfterDueDayEnabled: Boolean,
    isReminderForDueBillEnabled: Boolean,
    onReminderAfterDueDayEnableChange: (Boolean) -> Unit,
    onReminderForDueBillEnableChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.advance_reminder_settings),
            style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        CommonCheckBoxField(
            checked = isReminderAfterDueDayEnabled,
            onCheckedChange = onReminderAfterDueDayEnableChange,
            label = stringResource(Res.string.remind_everyday_after_due_day)
        )
        Spacer(modifier = Modifier.height(8.dp))
        CommonCheckBoxField(
            checked = isReminderForDueBillEnabled,
            onCheckedChange = onReminderForDueBillEnableChange,
            label = stringResource(Res.string.remind_everyday_for_due_bill)
        )
    }
}

internal data class ReminderState(
    val dueDayReminder: Int,
    val isReminderBeforeDueDayEnabled: Boolean,
    val isReminderForDueBillEnabled: Boolean
)

private fun Int.toDayReminderStringResource(): StringResource {
    return when (this) {
        1 -> Res.string.days_before
        3 -> Res.string.three_days_before
        5 -> Res.string.five_days_before
        7 -> Res.string.weeks_before
        else -> Res.string.bill
    }
}