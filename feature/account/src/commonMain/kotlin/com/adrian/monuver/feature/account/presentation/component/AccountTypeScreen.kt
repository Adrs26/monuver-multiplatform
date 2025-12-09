package com.adrian.monuver.feature.account.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.adrian.monuver.core.presentation.components.TransactionCategoryIcon
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.choose_account_type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountTypeScreen(
    onNavigateBack: () -> Unit,
    onTypeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.choose_account_type),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            DataProvider.provideAccountTypes().forEach { accountType ->
                AccountTypeListItem(
                    accountType = accountType,
                    modifier = Modifier
                        .debouncedClickable {
                            onTypeSelect(accountType)
                            onNavigateBack()
                        }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun AccountTypeListItem(
    accountType: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransactionCategoryIcon(
                icon = DatabaseCodeMapper.toAccountTypeIcon(accountType),
                backgroundColor = DatabaseCodeMapper.toAccountTypeColor(accountType),
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = stringResource(DatabaseCodeMapper.toAccountType(accountType)),
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