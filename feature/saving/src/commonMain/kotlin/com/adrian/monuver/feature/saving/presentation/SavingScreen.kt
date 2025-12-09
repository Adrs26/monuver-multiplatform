package com.adrian.monuver.feature.saving.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.presentation.components.CommonFloatingActionButton
import com.adrian.monuver.core.presentation.navigation.Saving as SavingRoute
import com.adrian.monuver.feature.saving.presentation.components.SavingAppBar
import com.adrian.monuver.feature.saving.presentation.components.SavingEmptyListContent
import com.adrian.monuver.feature.saving.presentation.components.SavingListContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SavingScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<SavingViewModel>()
    val totalCurrentAmount by viewModel.totalCurrentAmount.collectAsStateWithLifecycle()
    val savings by viewModel.savings.collectAsStateWithLifecycle()

    SavingContent(
        totalCurrentAmount = totalCurrentAmount ?: 0,
        savings = savings,
        onAction = { action ->
            when (action) {
                is SavingAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is SavingAction.NavigateToAddSaving -> {
                    navController.navigate(SavingRoute.Add)
                }
                is SavingAction.NavigateToInactiveSaving -> {
                    navController.navigate(SavingRoute.Inactive)
                }
                is SavingAction.NavigateToSavingDetail -> {
                    navController.navigate(SavingRoute.Detail(action.savingId))
                }
            }
        }
    )
}

@Composable
private fun SavingContent(
    totalCurrentAmount: Long,
    savings: List<Saving>,
    onAction: (SavingAction) -> Unit
) {
    Scaffold(
        topBar = {
            SavingAppBar(
                onNavigateBack = {
                    onAction(SavingAction.NavigateBack)
                },
                onNavigateToInactiveSaving = {
                    onAction(SavingAction.NavigateToInactiveSaving)
                }
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                onAction(SavingAction.NavigateToAddSaving)
            }
        }
    ) { innerPadding ->
        when {
            savings.isEmpty() -> {
                SavingEmptyListContent(
                    totalCurrentAmount = totalCurrentAmount,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
            else -> {
                SavingListContent(
                    totalCurrentAmount = totalCurrentAmount,
                    savings = savings,
                    onNavigateToSavingDetail = { savingId ->
                        onAction(SavingAction.NavigateToSavingDetail(savingId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}