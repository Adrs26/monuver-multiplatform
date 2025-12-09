package com.adrian.monuver.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.components.CommonFloatingActionButton
import com.adrian.monuver.core.presentation.navigation.Budget
import com.adrian.monuver.core.presentation.navigation.Main
import com.adrian.monuver.core.presentation.navigation.Transaction
import com.adrian.monuver.feature.analytics.presentation.AnalyticsScreen
import com.adrian.monuver.feature.budgeting.presentation.BudgetingScreen
import com.adrian.monuver.feature.home.presentation.HomeScreen
import com.adrian.monuver.feature.transaction.presentation.TransactionScreen
import com.adrian.monuver.main.components.BudgetWarningDialog
import com.adrian.monuver.main.components.MainNavigationBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.add_expense
import monuver.composeapp.generated.resources.add_income
import monuver.composeapp.generated.resources.transfer_account
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    rootNavController: NavHostController
) {
    val mainNavController = rememberNavController()

    val menuItems = listOf(
        Main.Home.toString(),
        Main.Transaction.toString(),
        Main.Budgeting.toString(),
        Main.Analytics.toString()
    )
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedMenu by rememberSaveable { mutableStateOf(menuItems[0]) }
    var showFab by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBudgetWarningDialog by remember { mutableStateOf(false) }

    val budgetWarningCondition = rootNavController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("warning_condition", 0)
        ?.collectAsStateWithLifecycle()

    val budgetCategory = rootNavController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("budget_category", 0)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(selectedMenu) {
        showFab = false
        delay(200)
        if (selectedMenu == menuItems[1] || selectedMenu == menuItems[2]) {
            showFab = true
        }
    }

    LaunchedEffect(budgetWarningCondition?.value, budgetCategory?.value) {
        val warningValue = budgetWarningCondition?.value
        val categoryValue = budgetCategory?.value

        if (warningValue != 0 && categoryValue != 0) {
            showBudgetWarningDialog = true
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                MainNavigationBar(
                    navController = mainNavController,
                    onNavigate = { selectedMenu = it },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 200)
                ) + fadeIn(animationSpec = tween(200)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 200)
                ) + fadeOut(animationSpec = tween(200))
            ) {
                CommonFloatingActionButton {
                    when (selectedMenu) {
                        menuItems[1] -> showBottomSheet = true
                        menuItems[2] -> rootNavController.navigate(Budget.Add)
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Main.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Main.Home> {
                HomeScreen(
                    rootNavController = rootNavController,
                    mainNavController = mainNavController
                )
            }

            composable<Main.Transaction> {
                TransactionScreen(rootNavController)
            }

            composable<Main.Budgeting> {
                BudgetingScreen(rootNavController)
            }

            composable<Main.Analytics> {
                AnalyticsScreen(rootNavController)
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            shape = BottomSheetDefaults.HiddenShape,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { }
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                BottomSheetMenu(
                    title = stringResource(Res.string.add_income)
                ) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            rootNavController.navigate(Transaction.Add(TransactionType.INCOME))
                        }
                    }
                }
                BottomSheetMenu(
                    title = stringResource(Res.string.add_expense)
                ) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            rootNavController.navigate(Transaction.Add(TransactionType.EXPENSE))
                        }
                    }
                }
                BottomSheetMenu(
                    title = stringResource(Res.string.transfer_account)
                ) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            rootNavController.navigate(Transaction.Transfer)
                        }
                    }
                }
            }
        }
    }

    if (showBudgetWarningDialog && budgetWarningCondition?.value != 0 && budgetCategory?.value != 0) {
        BudgetWarningDialog(
            budgetCategory = budgetCategory?.value ?: 0,
            budgetWarningCondition = budgetWarningCondition?.value ?: 0,
            onDismissRequest = {
                showBudgetWarningDialog = false
                rootNavController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("warning_condition", 0)
            }
        )
    }
}

@Composable
private fun BottomSheetMenu(
    title: String,
    onMenuSelect: () -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMenuSelect() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}