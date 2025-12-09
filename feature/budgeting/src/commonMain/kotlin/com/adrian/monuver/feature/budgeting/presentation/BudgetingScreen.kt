package com.adrian.monuver.feature.budgeting.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingAppBar
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingEmptyListContent
import com.adrian.monuver.feature.budgeting.presentation.components.BudgetingListContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BudgetingScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<BudgetingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    BudgetingContent(
        state = state,
        onAction = { action ->
            when (action) {
                BudgetingAction.NavigateToInactiveBudget -> {
                    navController.navigate(Budget.Inactive)
                }
                is BudgetingAction.NavigateToBudgetDetail -> {
                    navController.navigate(Budget.Detail(action.budgetId))
                }
            }
        }
    )
}

@Composable
private fun BudgetingContent(
    state: BudgetingState,
    onAction: (BudgetingAction) -> Unit
) {
    Scaffold(
        topBar = {
            BudgetingAppBar(
                onNavigateToInactiveBudget = {
                    onAction(BudgetingAction.NavigateToInactiveBudget)
                }
            )
        }
    ) { innerPadding ->
        when  {
            state.budgets.isEmpty() -> {
                BudgetingEmptyListContent(
                    totalMaxAmount = state.totalMaxAmount,
                    totalUsedAmount = state.totalUsedAmount,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
            else -> {
                BudgetingListContent(
                    totalMaxAmount = state.totalMaxAmount,
                    totalUsedAmount = state.totalUsedAmount,
                    budgets = state.budgets,
                    onNavigateToBudgetDetail = { budgetId ->
                        onAction(BudgetingAction.NavigateToBudgetDetail(budgetId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}