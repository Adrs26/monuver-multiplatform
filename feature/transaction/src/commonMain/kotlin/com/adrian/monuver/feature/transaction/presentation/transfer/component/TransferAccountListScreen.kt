package com.adrian.monuver.feature.transaction.presentation.transfer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.util.SelectAccountType
import com.adrian.monuver.core.presentation.components.AccountEmptyContent
import com.adrian.monuver.core.presentation.components.AccountListItem
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.transaction.generated.resources.Res
import monuver.feature.transaction.generated.resources.choose_destination_account
import monuver.feature.transaction.generated.resources.choose_source_account
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TransferAccountListScreen(
    selectAccountType: Int,
    accounts: List<Account>,
    selectedAccounts: List<Int>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (selectAccountType == SelectAccountType.SOURCE)
                    stringResource(Res.string.choose_source_account) else
                        stringResource(Res.string.choose_destination_account),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (accounts.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                items(
                    count = accounts.size,
                    key = { index -> accounts[index].id }
                ) { index ->
                    val isAccountSelected = accounts[index].id in selectedAccounts

                    AccountListItem(
                        account = accounts[index],
                        modifier = if (!isAccountSelected) {
                            Modifier
                                .debouncedClickable {
                                    onAccountSelect(accounts[index].id, accounts[index].name)
                                    onNavigateBack()
                                }
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        } else {
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        },
                        containerColor = if (!isAccountSelected) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                }
            }
        } else {
            AccountEmptyContent { onNavigateToAddAccount() }
        }
    }
}