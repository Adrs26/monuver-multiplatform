package com.adrian.monuver.feature.saving.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.adrian.monuver.core.presentation.components.AccountListScreen
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.navigation.Saving
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.feature.saving.presentation.SavingScreen
import com.adrian.monuver.feature.saving.presentation.addSaving.AddSavingScreen
import com.adrian.monuver.feature.saving.presentation.deposit.DepositScreen
import com.adrian.monuver.feature.saving.presentation.deposit.DepositViewModel
import com.adrian.monuver.feature.saving.presentation.editSaving.EditSavingScreen
import com.adrian.monuver.feature.saving.presentation.inactiveSaving.InactiveSavingScreen
import com.adrian.monuver.feature.saving.presentation.savingDetail.SavingDetailScreen
import com.adrian.monuver.feature.saving.presentation.withdraw.WithdrawScreen
import com.adrian.monuver.feature.saving.presentation.withdraw.WithdrawViewModel

fun NavGraphBuilder.savingNavGraph(
    navController: NavHostController
) {
    animatedComposable<Saving.Main> {
        SavingScreen(navController)
    }

    animatedComposable<Saving.Add> {
        AddSavingScreen(navController)
    }

    animatedComposable<Saving.Detail> { navBackStackEntry ->
        SavingDetailScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    animatedComposable<Saving.Edit> { navBackStackEntry ->
        EditSavingScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    animatedComposable<Saving.Inactive> {
        InactiveSavingScreen(navController)
    }

    navigation<Saving>(startDestination = Saving.Deposit()) {
        animatedComposable<Saving.Deposit> { navBackStackEntry ->
            DepositScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Saving.DepositAccount> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<DepositViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::setTransactionAccount,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }

        animatedComposable<Saving.Withdraw> { navBackStackEntry ->
            WithdrawScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Saving.WithdrawAccount> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<WithdrawViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::setTransactionAccount,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}