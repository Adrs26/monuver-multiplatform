package com.adrian.monuver.feature.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adrian.monuver.core.domain.common.ThemeState
import monuver.feature.settings.generated.resources.Res
import monuver.feature.settings.generated.resources.apply
import monuver.feature.settings.generated.resources.cancel
import monuver.feature.settings.generated.resources.choose_theme
import monuver.feature.settings.generated.resources.dark
import monuver.feature.settings.generated.resources.default_system
import monuver.feature.settings.generated.resources.light
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsThemeDialog(
    themeState: ThemeState,
    onThemeChange: (ThemeState) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTheme by remember { mutableStateOf(themeState) }

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
                Text(
                    text = stringResource(Res.string.choose_theme),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                ThemeRadioButton(
                    title = stringResource(Res.string.light),
                    selected = selectedTheme == ThemeState.Light,
                    onClick = { selectedTheme = ThemeState.Light }
                )
                ThemeRadioButton(
                    title = stringResource(Res.string.dark),
                    selected = selectedTheme == ThemeState.Dark,
                    onClick = { selectedTheme = ThemeState.Dark }
                )
                ThemeRadioButton(
                    title = stringResource(Res.string.default_system),
                    selected = selectedTheme == ThemeState.System,
                    onClick = { selectedTheme = ThemeState.System }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Button(
                        onClick = {
                            onThemeChange(selectedTheme)
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
private fun ThemeRadioButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 13.sp
            )
        )
    }
}