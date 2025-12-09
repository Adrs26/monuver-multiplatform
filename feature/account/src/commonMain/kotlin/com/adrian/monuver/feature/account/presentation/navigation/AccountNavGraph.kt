package com.adrian.monuver.feature.account.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.feature.account.presentation.AccountScreen
import com.adrian.monuver.feature.account.presentation.accountDetail.AccountDetailScreen
import com.adrian.monuver.feature.account.presentation.addAccount.AddAccountScreen
import com.adrian.monuver.feature.account.presentation.addAccount.AddAccountViewModel
import com.adrian.monuver.feature.account.presentation.component.AccountTypeScreen
import com.adrian.monuver.feature.account.presentation.editAccount.EditAccountScreen
import com.adrian.monuver.feature.account.presentation.editAccount.EditAccountViewModel

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    animatedComposable<Account.Main> {
        AccountScreen(
            navController = navController
        )
    }

    animatedComposable<Account.Detail> { navBackStackEntry ->
        AccountDetailScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    navigation<Account>(startDestination = Account.Add) {
        animatedComposable<Account.Add> { navBackStackEntry ->
            AddAccountScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Account.AddType> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<AddAccountViewModel>(navController)

            AccountTypeScreen(
                onNavigateBack = navController::navigateUp,
                onTypeSelect = viewModel::setAccountType
            )
        }

        animatedComposable<Account.Edit> { navBackStackEntry ->
            EditAccountScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Account.EditType> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<EditAccountViewModel>(navController)

            AccountTypeScreen(
                onNavigateBack = navController::navigateUp,
                onTypeSelect = viewModel::changeAccountType
            )
        }
    }
}