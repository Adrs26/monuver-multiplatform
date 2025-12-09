package com.adrian.monuver.feature.saving.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.list_active_save
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SavingEmptyListContent(
    totalCurrentAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SavingBalanceContent(
            totalCurrentAmount = totalCurrentAmount,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(Res.string.list_active_save),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        SavingEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    }
}