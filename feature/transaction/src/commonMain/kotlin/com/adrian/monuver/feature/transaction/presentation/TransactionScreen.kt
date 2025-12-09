package com.adrian.monuver.feature.transaction.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.transaction.presentation.components.TransactionAppBar
import com.adrian.monuver.feature.transaction.presentation.components.TransactionFilterDialog
import com.adrian.monuver.feature.transaction.presentation.components.TransactionListContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionScreen(
    navController: NavHostController,
) {
    val viewModel = koinViewModel<TransactionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    TransactionContent(
        state = state,
        onAction = { action ->
            when (action) {
                is TransactionAction.QueryChange -> {
                    viewModel.queryChange(action.query)
                }
                is TransactionAction.YearFilterOptionsRequest -> {
                    viewModel.getYearFilterOptions()
                }
                is TransactionAction.FilterApply -> {
                    viewModel.filterApply(action.filter)
                }
                is TransactionAction.NavigateToTransactionDetail -> {
                    navController.navigate(Transaction.Detail(action.transactionId))
                }
            }
        }
    )
}

@Composable
private fun TransactionContent(
    state: TransactionState,
    onAction: (TransactionAction) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showFilterDialog) {
        if (showFilterDialog) {
            onAction(TransactionAction.YearFilterOptionsRequest)
        }
    }

    Scaffold(
        topBar = {
            TransactionAppBar(
                query = state.query,
                onQueryChange = { query ->
                    onAction(TransactionAction.QueryChange(query))
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 12.dp, top = 48.dp, bottom = 12.dp),
                onFilterButtonClick = { showFilterDialog = true }
            )
        }
    ) { innerPadding ->
        TransactionListContent(
            transactions = state.transactions.collectAsLazyPagingItems(),
            onNavigateToTransactionDetail = {
                onAction(TransactionAction.NavigateToTransactionDetail(it))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

    if (showFilterDialog) {
        TransactionFilterDialog(
            filterState = state.filter,
            onDismissRequest = { showFilterDialog = false },
            onFilterApply = { filter ->
                onAction(TransactionAction.FilterApply(filter))
            }
        )
    }
}