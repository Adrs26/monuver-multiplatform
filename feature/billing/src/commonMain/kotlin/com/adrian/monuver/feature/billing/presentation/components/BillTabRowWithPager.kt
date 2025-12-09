package com.adrian.monuver.feature.billing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.adrian.monuver.feature.billing.domain.model.BillItem
import kotlinx.coroutines.launch
import monuver.feature.billing.generated.resources.Res
import monuver.feature.billing.generated.resources.due
import monuver.feature.billing.generated.resources.paid
import monuver.feature.billing.generated.resources.pending
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BillTabRowWithPager(
    pendingBills: List<BillItem>,
    dueBills: List<BillItem>,
    paidBills: LazyPagingItems<BillItem>,
    onNavigateToBillDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabTitles = listOf(
        stringResource(Res.string.pending),
        stringResource(Res.string.due),
        stringResource(Res.string.paid)
    )

    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage, true),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary else
                                    MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    PendingBillScreen(
                        bills = pendingBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
                1 -> {
                    DueBillScreen(
                        bills = dueBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
                2 -> {
                    PaidBillScreen(
                        bills = paidBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
            }
        }
    }
}