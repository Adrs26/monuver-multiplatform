package com.adrian.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.presentation.theme.Green600
import com.adrian.monuver.core.presentation.theme.Red600
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.add_amount
import monuver.feature.saving.generated.resources.withdraw_amount
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SavingDetailButtonContent(
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SavingDetailButton(
            text = stringResource(Res.string.add_amount),
            icon = Icons.Default.ArrowUpward,
            color = Green600,
            onClick = onNavigateToDeposit,
            modifier = Modifier.weight(1f)
        )
        SavingDetailButton(
            text = stringResource(Res.string.withdraw_amount),
            icon = Icons.Default.ArrowDownward,
            color = Red600,
            onClick = onNavigateToWithdraw,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SavingDetailButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = color,
                fontSize = 11.sp
            )
        )
    }
}