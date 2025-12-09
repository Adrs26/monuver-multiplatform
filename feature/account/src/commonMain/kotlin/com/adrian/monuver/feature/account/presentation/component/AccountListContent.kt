package com.adrian.monuver.feature.account.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.presentation.components.AccountListItem
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.your_list_account
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountListContent(
    accounts: List<Account>,
    balance: Long,
    onNavigateToAccountDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        item {
            AccountBalanceContent(
                balance = balance,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(Res.string.your_list_account),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = accounts.size,
            key = { index -> accounts[index].id }
        ) { index ->
            AccountListItem(
                account = accounts[index],
                modifier = Modifier
                    .debouncedClickable { onNavigateToAccountDetail(accounts[index].id) }
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}