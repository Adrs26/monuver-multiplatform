package com.adrian.monuver.feature.budgeting.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.adrian.monuver.core.presentation.components.DebouncedIconButton
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.budgeting_menu
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetingAppBar(
    onNavigateToInactiveBudget: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.budgeting_menu),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        actions = {
            DebouncedIconButton(
                onClick = onNavigateToInactiveBudget
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}