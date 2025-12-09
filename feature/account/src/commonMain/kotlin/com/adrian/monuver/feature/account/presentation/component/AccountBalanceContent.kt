package com.adrian.monuver.feature.account.presentation.component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.toRupiah
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.total_account_balance
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountBalanceContent(
    balance: Long,
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
                text = stringResource(Res.string.total_account_balance),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = balance.toRupiah(),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
            )
        }
    }
}