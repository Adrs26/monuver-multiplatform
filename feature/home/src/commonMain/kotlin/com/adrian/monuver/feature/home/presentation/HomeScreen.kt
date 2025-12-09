package com.adrian.monuver.feature.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.navigation.Main
import com.adrian.monuver.core.presentation.navigation.Saving
import com.adrian.monuver.core.presentation.navigation.Settings
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.home.presentation.components.HomeActionButtonContent
import com.adrian.monuver.feature.home.presentation.components.HomeAppBar
import com.adrian.monuver.feature.home.presentation.components.HomeBalanceContent
import com.adrian.monuver.feature.home.presentation.components.HomeBudgetContent
import com.adrian.monuver.feature.home.presentation.components.HomeRecentTransactionsContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    mainNavController: NavHostController
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.NavigateToBill -> {
                    rootNavController.navigate(Billing.Main)
                }
                is HomeAction.NavigateToSaving -> {
                    rootNavController.navigate(Saving.Main)
                }
                is HomeAction.NavigateToSettings -> {
                    rootNavController.navigate(Settings.Main)
                }
                is HomeAction.NavigateToAccount -> {
                    rootNavController.navigate(Account.Main)
                }
                is HomeAction.NavigateToAddIncome -> {
                    rootNavController.navigate(Transaction.Add(TransactionType.INCOME))
                }
                is HomeAction.NavigateToAddExpense -> {
                    rootNavController.navigate(Transaction.Add(TransactionType.EXPENSE))
                }
                is HomeAction.NavigateToTransfer -> {
                    rootNavController.navigate(Transaction.Transfer)
                }
                is HomeAction.NavigateToAddBudget -> {
                    rootNavController.navigate(Budget.Add)
                }
                is HomeAction.NavigateToTransaction -> {
                    mainNavController.navigate(Main.Transaction) {
                        popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
                is HomeAction.NavigateToTransactionDetail -> {
                    rootNavController.navigate(Transaction.Detail(action.transactionId))
                }
                is HomeAction.NavigateToBudgeting -> {
                    mainNavController.navigate(Main.Budgeting) {
                        popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            }
        }
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            HomeAppBar(
                onNavigateToBill = {
                    onAction(HomeAction.NavigateToBill)
                },
                onNavigateToSave = {
                    onAction(HomeAction.NavigateToSaving)
                },
                onNavigateToSettings = {
                    onAction(HomeAction.NavigateToSettings)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeBalanceContent(
                totalBalance = state.totalBalance,
                onNavigateToAccount = {
                    onAction(HomeAction.NavigateToAccount)
                },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            HomeActionButtonContent(
                onNavigateToAddIncomeTransaction = {
                    onAction(HomeAction.NavigateToAddIncome)
                },
                onNavigateToAddExpenseTransaction = {
                    onAction(HomeAction.NavigateToAddExpense)
                },
                onNavigateToTransfer = {
                    onAction(HomeAction.NavigateToTransfer)
                },
                onNavigateToAddBudget = {
                    onAction(HomeAction.NavigateToAddBudget)
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 24.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            HomeRecentTransactionsContent(
                recentTransactions = state.recentTransactions,
                onNavigateToTransaction = {
                    onAction(HomeAction.NavigateToTransaction)
                },
                onNavigateToTransactionDetail = {
                    onAction(HomeAction.NavigateToTransactionDetail(it))
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            HomeBudgetContent(
                totalUsedAmount = state.budgetSummary.totalUsedAmount,
                totalMaxAmount = state.budgetSummary.totalMaxAmount,
                onNavigateToBudgeting = {
                    onAction(HomeAction.NavigateToBudgeting)
                },
                modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 32.dp)
            )
        }
    }
}