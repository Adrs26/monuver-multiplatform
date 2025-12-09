package com.adrian.monuver.feature.billing.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.billing.domain.model.BillItem

@Composable
internal fun DueBillScreen(
    bills: List<BillItem>,
    onNavigateToBillDetail: (Long) -> Unit
) {
    if (bills.isEmpty()) {
        BillEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = bills.size,
                key = { index -> bills[index].id }
            ) { index ->
                BillListItem(
                    bill = bills[index],
                    modifier = Modifier
                        .debouncedClickable { onNavigateToBillDetail(bills[index].id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

