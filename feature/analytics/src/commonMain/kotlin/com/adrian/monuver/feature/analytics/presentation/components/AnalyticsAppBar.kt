package com.adrian.monuver.feature.analytics.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.util.DataProvider
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import monuver.feature.analytics.generated.resources.Res
import monuver.feature.analytics.generated.resources.analytics_menu
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalyticsAppBar(
    monthValue: Int,
    yearValue: Int,
    yearFilterOptions: List<Int>,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.analytics_menu),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        actions = {
            MonthFilterDropdown(
                monthValue = monthValue,
                onMonthChange = onMonthChange,
                modifier = Modifier.padding(end = 16.dp)
            )
            YearFilterDropdown(
                yearValue = yearValue,
                yearFilterOptions = yearFilterOptions,
                onYearChange = onYearChange,
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun MonthFilterDropdown(
    monthValue: Int,
    onMonthChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val monthFilterOptions = DataProvider.provideMonthFilterOptions().drop(1)

    Box(
        modifier = modifier
    ) {
        AnalyticsFilterDropdown(
            value = stringResource(DatabaseCodeMapper.toFullMonth(monthValue)),
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 150.dp),
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            monthFilterOptions.forEach { month ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onMonthChange(month)
                            expanded = false
                        }
                        .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = if (month == monthValue) MaterialTheme.colorScheme.onSurface else
                            MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = stringResource(DatabaseCodeMapper.toFullMonth(month)),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun YearFilterDropdown(
    yearValue: Int,
    yearFilterOptions: List<Int>,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        AnalyticsFilterDropdown(
            value = yearValue.toString(),
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 150.dp),
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            yearFilterOptions.forEach { year ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onYearChange(year)
                            expanded = false
                        }
                        .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = if (year == yearValue) MaterialTheme.colorScheme.onSurface else
                            MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}