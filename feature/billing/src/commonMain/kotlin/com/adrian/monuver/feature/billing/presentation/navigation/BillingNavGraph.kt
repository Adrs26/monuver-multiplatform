package com.adrian.monuver.feature.billing.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.components.AccountListScreen
import com.adrian.monuver.core.presentation.components.TransactionCategoryScreen
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.navigation.Billing
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.feature.billing.presentation.BillingScreen
import com.adrian.monuver.feature.billing.presentation.addBill.AddBillScreen
import com.adrian.monuver.feature.billing.presentation.billDetail.BillDetailScreen
import com.adrian.monuver.feature.billing.presentation.billPayment.BillPaymentScreen
import com.adrian.monuver.feature.billing.presentation.billPayment.BillPaymentViewModel
import com.adrian.monuver.feature.billing.presentation.editBill.EditBillScreen

fun NavGraphBuilder.billingNavGraph(
    navController: NavHostController
) {
    animatedComposable<Billing.Main> {
        BillingScreen(navController)
    }

    animatedComposable<Billing.Add> {
        AddBillScreen(navController)
    }

    animatedComposable<Billing.Detail> { navBackStackEntry ->
        BillDetailScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    animatedComposable<Billing.Edit> { navBackStackEntry ->
        EditBillScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    navigation<Billing>(startDestination = Billing.Payment()) {
        animatedComposable<Billing.Payment> { navBackStackEntry ->
            BillPaymentScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Billing.PaymentCategory> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<BillPaymentViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = TransactionType.EXPENSE,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::setTransactionCategory
            )
        }

        animatedComposable<Billing.PaymentAccount> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<BillPaymentViewModel>(navController)
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