package com.adrian.monuver.feature.billing.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.billing.domain.model.BillItem

@Composable
internal fun PaidBillScreen(
    bills: LazyPagingItems<BillItem>,
    onNavigateToBillDetail: (Long) -> Unit
) {
    if (bills.itemCount == 0 && bills.loadState.refresh is LoadState.NotLoading) {
        BillEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = bills.itemCount,
                key = { index -> bills[index]?.id!! }
            ) { index ->
                bills[index]?.let { bill ->
                    BillListItem(
                        bill = bill,
                        modifier = Modifier
                            .debouncedClickable { onNavigateToBillDetail(bill.id) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            when (bills.loadState.append) {
                is LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                is LoadState.Error -> {
                    val error = (bills.loadState.append as LoadState.Error).error
                    item { Text("Error: $error") }
                }
                else -> {}
            }
        }
    }
}