package com.adrian.monuver.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.util.DataProvider
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.choose_expense_category
import monuver.core.presentation.generated.resources.choose_income_category
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionCategoryScreen(
    transactionType: Int,
    onNavigateBack: () -> Unit,
    onCategorySelect: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactionCategory = if (transactionType == TransactionType.INCOME)
        DataProvider.provideIncomeCategories() else DataProvider.provideExpenseCategories()

    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (transactionType == TransactionType.INCOME)
                    stringResource(Res.string.choose_income_category) else
                        stringResource(Res.string.choose_expense_category),
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
            transactionCategory.forEach { (parentCategory, childCategories) ->
                CategorySection(
                    transactionType = transactionType,
                    parentCategory = parentCategory,
                    childCategories = childCategories,
                    onNavigateBack = onNavigateBack,
                    onCategorySelect = onCategorySelect,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CategorySection(
    transactionType: Int,
    parentCategory: Int,
    childCategories: List<Int>,
    onNavigateBack: () -> Unit,
    onCategorySelect: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacerWeight = when {
        childCategories.size % 4 == 1 -> 3f
        childCategories.size % 4 == 2 -> 2f
        childCategories.size % 4 == 3 -> 1f
        else -> 0f
    }

    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = MaterialTheme.shapes.medium
        ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(parentCategory)),
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 4
            ) {
                childCategories.forEach { childCategory ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .debouncedClickable {
                                if (transactionType == TransactionType.INCOME) {
                                    onCategorySelect(childCategory, childCategory)
                                } else {
                                    onCategorySelect(parentCategory, childCategory)
                                }
                                onNavigateBack()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TransactionCategoryIcon(
                            icon = DatabaseCodeMapper.toChildCategoryIcon(childCategory),
                            backgroundColor = DatabaseCodeMapper.toParentCategoryIconBackground(
                                if (transactionType == TransactionType.INCOME) childCategory else
                                    parentCategory
                            ),
                            modifier = modifier.size(42.dp)
                        )
                        Text(
                            text = stringResource(DatabaseCodeMapper.toChildCategoryTitle(childCategory)),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                if (spacerWeight > 0) {
                    Spacer(modifier = Modifier.weight(spacerWeight))
                }
            }
        }
    }
}