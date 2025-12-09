package com.adrian.monuver.feature.budgeting.presentation.inactiveBudget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.budgeting.domain.model.BudgetItem
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingEmptyAnimation
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingListItem
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.inactive_budget
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InactiveBudgetScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<InactiveBudgetViewModel>()
    val budgets = viewModel.budgets.collectAsLazyPagingItems()

    InactiveBudgetContent(
        budgets = budgets,
        onNavigateBack = navController::navigateUp,
        onNavigateToBudgetDetail = { budgetId ->
            navController.navigate(Budget.Detail(budgetId))
        }
    )
}

@Composable
private fun InactiveBudgetContent(
    budgets: LazyPagingItems<BudgetItem>,
    onNavigateBack: () -> Unit,
    onNavigateToBudgetDetail: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.inactive_budget),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (budgets.itemCount == 0 && budgets.loadState.refresh is LoadState.NotLoading) {
            BudgetingEmptyAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(
                    count = budgets.itemCount,
                    key = { index -> budgets[index]?.id!! }
                ) { index ->
                    budgets[index]?.let { budget ->
                        BudgetingListItem(
                            budget = budget,
                            modifier = Modifier.debouncedClickable {
                                onNavigateToBudgetDetail(budget.id)
                            }
                        )
                    }
                }

                when (budgets.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                    }
                    is LoadState.Error -> {
                        val error = (budgets.loadState.append as LoadState.Error).error
                        item { Text("Error: $error") }
                    }
                    else -> {}
                }
            }
        }
    }
}