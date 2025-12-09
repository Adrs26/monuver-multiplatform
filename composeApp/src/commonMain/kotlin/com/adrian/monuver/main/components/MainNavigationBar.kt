package com.adrian.monuver.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.adrian.monuver.core.presentation.navigation.Main
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.analytics_menu
import monuver.composeapp.generated.resources.budgeting_menu
import monuver.composeapp.generated.resources.home_menu
import monuver.composeapp.generated.resources.transaction_menu
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MainNavigationBar(
    navController: NavHostController,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        MainNavigationDestination.entries.forEachIndexed { _, destination ->
            val selected = isItemSelected(currentRoute, destination.route.toString())
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                    onNavigate(destination.route.toString())
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.icon,
                        contentDescription = stringResource(destination.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.title),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

private enum class MainNavigationDestination(
    val title: StringResource,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: Any
) {
    HOME(
        title = Res.string.home_menu,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Main.Home
    ),
    TRANSACTION(
        title = Res.string.transaction_menu,
        icon = Icons.Outlined.Receipt,
        selectedIcon = Icons.Filled.Receipt,
        route = Main.Transaction
    ),
    BUDGETING(
        title = Res.string.budgeting_menu,
        icon = Icons.Outlined.PieChartOutline,
        selectedIcon = Icons.Filled.PieChart,
        route = Main.Budgeting
    ),
    ANALYTICS(
        title = Res.string.analytics_menu,
        icon = Icons.Outlined.InsertChart,
        selectedIcon = Icons.Filled.InsertChart,
        route = Main.Analytics
    )
}

private fun isItemSelected(currentRoute: String?, itemRoute: String): Boolean {
    if (currentRoute == null) return false
    return currentRoute.contains(itemRoute, ignoreCase = true)
}