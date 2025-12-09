package com.adrian.monuver.feature.budgeting.presentation.addBudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.DataProvider
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.budgeting.generated.resources.Res
import monuver.feature.budgeting.generated.resources.choose_expense_category
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AddBudgetCategoryScreen(
    onNavigateBack: () -> Unit,
    onCategorySelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.choose_expense_category),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            DataProvider.provideExpenseParentCategories().forEach { category ->
                BudgetCategoryListItem(
                    category = category,
                    modifier = Modifier
                        .debouncedClickable {
                            onCategorySelect(category)
                            onNavigateBack()
                        }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun BudgetCategoryListItem(
    category: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = DatabaseCodeMapper.toParentCategoryIconBackground(category),
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .size(32.dp)
            )
            Text(
                text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
            )
        }
    }
}