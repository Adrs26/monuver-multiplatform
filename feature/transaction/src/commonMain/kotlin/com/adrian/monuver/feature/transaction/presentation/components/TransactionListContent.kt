package com.adrian.monuver.feature.transaction.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.core.presentation.components.TransactionListItem
import com.adrian.monuver.core.presentation.util.debouncedClickable
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.feature.transaction.generated.resources.Res

@Composable
internal fun TransactionListContent(
    transactions: LazyPagingItems<TransactionItem>,
    modifier: Modifier = Modifier,
    onNavigateToTransactionDetail: (Long) -> Unit
) {
    if (transactions.itemCount == 0 && transactions.loadState.refresh is LoadState.NotLoading) {
        TransactionEmptyAnimation(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(
                count = transactions.itemCount,
                key = { index -> transactions[index]?.id!! }
            ) { index ->
                transactions[index]?.let { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        modifier = Modifier
                            .animateItem()
                            .debouncedClickable { onNavigateToTransactionDetail(transaction.id) }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }

            when (transactions.loadState.append) {
                is LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                is LoadState.Error -> {
                    val error = (transactions.loadState.append as LoadState.Error).error
                    item { Text("Error: $error") }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun TransactionEmptyAnimation(
    modifier: Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/empty.json").decodeToString())
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress }
            ),
            contentDescription = "Lottie Animation"
        )
    }
}