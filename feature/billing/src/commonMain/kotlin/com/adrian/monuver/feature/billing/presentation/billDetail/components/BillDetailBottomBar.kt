package com.adrian.monuver.feature.billing.presentation.billDetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.cancel_bill_payment
import monuver.feature.billing.generated.resources.pay_bill
import org.jetbrains.compose.resources.stringResource

@Composable
fun BillDetailBottomBar(
    isPaid: Boolean,
    onProcessPayment: () -> Unit,
    onCancelPayment: () -> Unit,
    debounceMillis: Long = 600L
) {
    val scope = rememberCoroutineScope()
    var enabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp
        )
        if (!isPaid) {
            Button(
                onClick = {
                    if (enabled) {
                        enabled = false
                        onProcessPayment()
                        scope.launch {
                            delay(debounceMillis)
                            enabled = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.pay_bill),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            OutlinedButton(
                onClick = {
                    if (enabled) {
                        enabled = false
                        onCancelPayment()
                        scope.launch {
                            delay(debounceMillis)
                            enabled = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = stringResource(Res.string.cancel_bill_payment),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}