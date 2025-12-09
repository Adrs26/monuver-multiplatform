package com.adrian.monuver.feature.budgeting.presentation.budgetDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Budget
import com.adrian.monuver.core.domain.model.TransactionItem
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components.BudgetDetailAppBar
import com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components.BudgetDetailEmptyListContent
import com.adrian.monuver.feature.budgeting.presentation.budgetDetail.components.BudgetDetailListContent
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.delete_this_budgeting
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.adrian.monuver.core.presentation.navigation.Budget as BudgetRoute

@Composable
fun BudgetDetailScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<BudgetDetailViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val budget by viewModel.budget.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    budget?.let { budget ->
        BudgetDetailContent(
            budget = budget,
            transactions = transactions,
            onAction = { action ->
                when (action) {
                    is BudgetDetailAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is BudgetDetailAction.NavigateToEditBudget -> {
                        navController.navigate(BudgetRoute.Edit(action.budgetId))
                    }
                    is BudgetDetailAction.NavigateToTransactionDetail -> {
                        navController.navigate(Transaction.Detail(action.transactionId))
                    }
                    is BudgetDetailAction.RemoveCurrentBudget -> {
                        viewModel.deleteBudget(action.budgetId)
                    }
                }
            }
        )
    }
}

@Composable
private fun BudgetDetailContent(
    budget: Budget,
    transactions: List<TransactionItem>,
    onAction: (BudgetDetailAction) -> Unit,
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BudgetDetailAppBar(
                isBudgetActive = budget.isActive,
                onNavigateBack = {
                    onAction(BudgetDetailAction.NavigateBack)
                },
                onNavigateToEditBudget = {
                    onAction(BudgetDetailAction.NavigateToEditBudget(budget.id))
                },
                onRemoveBudget = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        when  {
            transactions.isEmpty() -> {
                BudgetDetailEmptyListContent(
                    budget = budget,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
            else -> {
                BudgetDetailListContent(
                    budget = budget,
                    transactions = transactions,
                    onNavigateToTransactionDetail = { transactionId ->
                        onAction(BudgetDetailAction.NavigateToTransactionDetail(transactionId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.delete_this_budgeting),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                onAction(BudgetDetailAction.RemoveCurrentBudget(budget.id))
                onAction(BudgetDetailAction.NavigateBack)
            }
        )
    }
}