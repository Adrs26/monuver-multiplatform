package com.adrian.monuver.feature.settings.presentation.export.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import monuver.feature.settings.generated.resources.Res
import monuver.feature.settings.generated.resources.newest_date
import monuver.feature.settings.generated.resources.oldest_date
import monuver.feature.settings.generated.resources.sort_by
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportSortTypeRadioField(
    selectedSortType: Int,
    onSortTypeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.sort_by),
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 1,
                    onClick = { onSortTypeSelect(1) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(Res.string.oldest_date),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 2,
                    onClick = { onSortTypeSelect(2) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(Res.string.newest_date),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
            }
        }
    }
}