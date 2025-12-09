package com.adrian.monuver.feature.analytics.presentation.analyticsTransaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.TransactionListItem
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.analytics.generated.resources.Res
import monuver.feature.analytics.generated.resources.analytics_menu
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsTransactionScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<AnalyticsTransactionViewModel>()

    AnalyticsTransactionContent(
        category = viewModel.state.category,
        transactions = viewModel.state.transactions,
        onAction = { action ->
            when (action) {
                is AnalyticsTransactionAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AnalyticsTransactionAction.NavigateToTransactionDetail  -> {
                    navController.navigate(Transaction.Detail(action.transactionId))
                }
            }
        }
    )
}

@Composable
internal fun AnalyticsTransactionContent(
    category: StringResource,
    transactions: List<TransactionItem>,
    onAction: (AnalyticsTransactionAction) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(category),
                onNavigateBack = {
                    onAction(AnalyticsTransactionAction.NavigateBack)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                count = transactions.size,
                key = { index -> transactions[index].id }
            ) { index ->
                TransactionListItem(
                    transaction = transactions[index],
                    modifier = Modifier
                        .debouncedClickable {
                            onAction(
                                AnalyticsTransactionAction.NavigateToTransactionDetail(
                                    transactions[index].id
                                )
                            )
                        }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}

internal data class AnalyticsTransactionState(
    val category: StringResource = Res.string.analytics_menu,
    val transactions: List<TransactionItem> = emptyList(),
)

internal sealed interface AnalyticsTransactionAction {
    data object NavigateBack : AnalyticsTransactionAction
    data class  NavigateToTransactionDetail(val transactionId: Long) : AnalyticsTransactionAction
}