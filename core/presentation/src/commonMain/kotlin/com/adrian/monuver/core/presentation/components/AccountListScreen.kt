package com.adrian.monuver.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.choose_account
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountListScreen(
    accounts: List<Account>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.choose_account),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (accounts.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    count = accounts.size,
                    key = { index -> accounts[index].id }
                ) { index ->
                    AccountListItem(
                        account = accounts[index],
                        modifier = Modifier
                            .debouncedClickable {
                                onAccountSelect(accounts[index].id, accounts[index].name)
                                onNavigateBack()
                            }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }
        } else {
            AccountEmptyContent {
                onNavigateToAddAccount()
            }
        }
    }
}