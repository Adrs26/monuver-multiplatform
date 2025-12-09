package com.adrian.monuver.feature.saving.presentation.inactiveSaving

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.util.debouncedClickable
import com.adrian.monuver.feature.saving.presentation.components.SavingEmptyAnimation
import com.adrian.monuver.feature.saving.presentation.components.SavingListItem
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.inactive_save
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.adrian.monuver.core.presentation.navigation.Saving as SavingRoute

@Composable
fun InactiveSavingScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<InactiveSavingViewModel>()
    val savings by viewModel.savings.collectAsStateWithLifecycle()

    InactiveSavingContent(
        savings = savings,
        onNavigateBack = navController::navigateUp,
        onNavigateToSavingDetail = { savingId ->
            navController.navigate(SavingRoute.Detail(savingId))
        }
    )
}

@Composable
private fun InactiveSavingContent(
    savings: List<Saving>,
    onNavigateBack: () -> Unit,
    onNavigateToSavingDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.inactive_save),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (savings.isEmpty()) {
            SavingEmptyAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(
                    count = savings.size,
                    key = { index -> savings[index].id }
                ) { index ->
                    SavingListItem(
                        saving = savings[index],
                        modifier = Modifier.debouncedClickable { onNavigateToSavingDetail(savings[index].id) }
                    )
                }
            }
        }
    }
}