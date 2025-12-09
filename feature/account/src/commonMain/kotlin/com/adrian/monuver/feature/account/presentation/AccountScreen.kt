package com.adrian.monuver.feature.account.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonFloatingActionButton
import com.adrian.monuver.feature.account.presentation.component.AccountEmptyListContent
import com.adrian.monuver.feature.account.presentation.component.AccountListContent
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.adrian.monuver.core.presentation.navigation.Account as AccountRoute

@Composable
fun AccountScreen(
    navController: NavHostController
) {
    val viewModel = koinViewModel<AccountViewModel>()
    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()

    AccountContent(
        accounts = accounts,
        balance = balance,
        onAction = { action ->
            when (action) {
                is AccountAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AccountAction.NavigateToAddAccount -> {
                    navController.navigate(AccountRoute.Add)
                }
                is AccountAction.NavigateToAccountDetail -> {
                    navController.navigate(AccountRoute.Detail(action.accountId))
                }
            }
        }
    )
}

@Composable
internal fun AccountContent(
    accounts: List<Account>,
    balance: Long,
    onAction: (AccountAction) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.account),
                onNavigateBack = {
                    onAction(AccountAction.NavigateBack)
                }
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                onAction(AccountAction.NavigateToAddAccount)
            }
        }
    ) { innerPadding ->
        when  {
            accounts.isEmpty() -> {
                AccountEmptyListContent(
                    balance = balance,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
            else -> {
                AccountListContent(
                    accounts = accounts,
                    balance = balance,
                    onNavigateToAccountDetail = { accountId ->
                        onAction(AccountAction.NavigateToAccountDetail(accountId))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}