package com.adrian.monuver.feature.transaction.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.adrian.monuver.core.domain.util.SelectAccountType
import com.adrian.monuver.core.presentation.components.AccountListScreen
import com.adrian.monuver.core.presentation.components.TransactionCategoryScreen
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.feature.transaction.presentation.addTransaction.AddTransactionScreen
import com.adrian.monuver.feature.transaction.presentation.addTransaction.AddTransactionViewModel
import com.adrian.monuver.feature.transaction.presentation.editTransaction.EditTransactionScreen
import com.adrian.monuver.feature.transaction.presentation.editTransaction.EditTransactionViewModel
import com.adrian.monuver.feature.transaction.presentation.transactionDetail.TransactionDetailScreen
import com.adrian.monuver.feature.transaction.presentation.transfer.TransferScreen
import com.adrian.monuver.feature.transaction.presentation.transfer.TransferViewModel
import com.adrian.monuver.feature.transaction.presentation.transfer.component.TransferAccountListScreen

fun NavGraphBuilder.transactionNavGraph(
    navController: NavHostController
) {
    animatedComposable<Transaction.Detail> { navBackStackEntry ->
        TransactionDetailScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    navigation<Transaction>(startDestination = Transaction.Add()) {
        animatedComposable<Transaction.Add> { navBackStackEntry ->
            AddTransactionScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Transaction.AddCategory> { navBackStackEntry ->
            val type = navBackStackEntry.toRoute<Transaction.AddCategory>().type
            val viewModel = navBackStackEntry.sharedKoinViewModel<AddTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = type,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::setTransactionCategory
            )
        }

        animatedComposable<Transaction.AddAccount> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::setTransactionAccount,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }

        animatedComposable<Transaction.Transfer> { navBackStackEntry ->
            TransferScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Transaction.TransferAccount> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<Transaction.TransferAccount>()
            val viewModel = navBackStackEntry.sharedKoinViewModel<TransferViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()
            val selectedAccounts by viewModel.selectedAccounts.collectAsStateWithLifecycle()

            TransferAccountListScreen(
                selectAccountType = args.type,
                accounts = accounts,
                selectedAccounts = selectedAccounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = { accountId, accountName ->
                    if (args.type == SelectAccountType.SOURCE) {
                        viewModel.setSourceAccount(accountId, accountName)
                    } else {
                        viewModel.setDestinationAccount(accountId, accountName)
                    }
                },
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }

        animatedComposable<Transaction.Edit> { navBackStackEntry ->
            EditTransactionScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Transaction.EditCategory> { navBackStackEntry ->
            val type = navBackStackEntry.toRoute<Transaction.EditCategory>().type
            val viewModel = navBackStackEntry.sharedKoinViewModel<EditTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = type,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::setTransactionCategory
            )
        }
    }
}