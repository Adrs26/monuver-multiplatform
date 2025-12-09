package com.adrian.monuver.feature.budgeting.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.util.animatedComposable
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.feature.budgeting.presentation.addBudget.AddBudgetScreen
import com.adrian.monuver.feature.budgeting.presentation.addBudget.AddBudgetViewModel
import com.adrian.monuver.feature.budgeting.presentation.addBudget.components.AddBudgetCategoryScreen
import com.adrian.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailScreen
import com.adrian.monuver.feature.budgeting.presentation.editBudget.EditBudgetScreen
import com.adrian.monuver.feature.budgeting.presentation.inactiveBudget.InactiveBudgetScreen

fun NavGraphBuilder.budgetingNavGraph(
    navController: NavHostController
) {
    animatedComposable<Budget.Detail> { navBackStackEntry ->
        BudgetDetailScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    animatedComposable<Budget.Edit> { navBackStackEntry ->
        EditBudgetScreen(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }

    animatedComposable<Budget.Inactive> {
        InactiveBudgetScreen(navController)
    }

    navigation<Budget>(startDestination = Budget.Add) {
        animatedComposable<Budget.Add> { navBackStackEntry ->
            AddBudgetScreen(
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        }

        animatedComposable<Budget.AddCategory> { navBackStackEntry ->
            val viewModel = navBackStackEntry.sharedKoinViewModel<AddBudgetViewModel>(navController)

            AddBudgetCategoryScreen(
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::setCategory
            )
        }
    }
}