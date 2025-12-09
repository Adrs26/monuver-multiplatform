package com.adrian.monuver.feature.home.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.presentation.components.DebouncedIconButton
import monuver.feature.home.generated.resources.Res
import monuver.feature.home.generated.resources.app_name
import monuver.feature.home.generated.resources.ic_overview
import monuver.feature.home.generated.resources.ic_savings
import monuver.feature.home.generated.resources.ic_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeAppBar(
    onNavigateToBill: () -> Unit,
    onNavigateToSave: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            )
        },
        actions = {
            HomeIconButton(
                icon = Res.drawable.ic_overview,
                onClick = onNavigateToBill
            )
            HomeIconButton(
                icon = Res.drawable.ic_savings,
                onClick = onNavigateToSave
            )
            HomeIconButton(
                icon = Res.drawable.ic_settings,
                onClick = onNavigateToSettings
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun HomeIconButton(
    icon: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DebouncedIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}