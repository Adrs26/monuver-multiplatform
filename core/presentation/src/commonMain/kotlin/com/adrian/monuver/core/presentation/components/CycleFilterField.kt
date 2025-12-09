package com.adrian.monuver.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.cycle
import org.jetbrains.compose.resources.stringResource

@Composable
fun CycleFilterField(
    cycles: List<Int>,
    selectedCycle: Int,
    onCycleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(Res.string.cycle),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.extraSmall
                )
        ) {
            cycles.forEach { cycle ->
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(
                            if (cycle == selectedCycle) MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.surface,
                        )
                        .debouncedClickable { onCycleChange(cycle) }
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(DatabaseCodeMapper.toCycle(cycle)),
                        modifier = Modifier.padding(vertical = 10.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (cycle == selectedCycle) MaterialTheme.colorScheme.onPrimary else
                                MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}