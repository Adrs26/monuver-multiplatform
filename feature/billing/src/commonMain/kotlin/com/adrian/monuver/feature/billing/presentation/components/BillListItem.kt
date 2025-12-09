package com.adrian.monuver.feature.billing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.theme.Red600
import com.adrian.monuver.feature.billing.domain.model.BillItem
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.bill_sub_information
import monuver.feature.billing.generated.resources.paid_bill_date_information
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BillListItem(
    bill: BillItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = bill.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )
            BillSubInformationText(
                status = bill.status,
                isRecurring = bill.isRecurring,
                dueDate = bill.dueDate,
                paidDate = bill.paidDate,
                nowPaidPeriod = bill.nowPaidPeriod
            )
        }
        Text(
            text = bill.amount.toRupiah(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BillSubInformationText(
    status: Int,
    isRecurring: Boolean,
    dueDate: String,
    paidDate: String,
    nowPaidPeriod: Int
) {
    when {
        status != 3 && isRecurring -> {
            Text(
                text = stringResource(
                    Res.string.bill_sub_information,
                    DateHelper.formatToReadable(dueDate),
                    "Pembayaran ke-${nowPaidPeriod}"
                ),
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = getBillDateInformationColor(status),
                    fontSize = 11.sp
                )
            )
        }
        status != 3 && !isRecurring -> {
            Text(
                text = DateHelper.formatToReadable(dueDate),
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = getBillDateInformationColor(status),
                    fontSize = 11.sp
                )
            )
        }
        else -> {
            Text(
                text = stringResource(
                    Res.string.paid_bill_date_information,
                    DateHelper.formatToReadable(paidDate)
                ),
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun getBillDateInformationColor(billStatus: Int): Color {
    return when (billStatus) {
        1, 3 -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> Red600
    }
}