package com.adrian.monuver.feature.account.presentation.accountDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.util.isActivateAccountSuccess
import com.adrian.monuver.core.domain.util.isDeactivateAccountSuccess
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.components.DetailDataContent
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.feature.account.presentation.accountDetail.components.AccountDetailAppBar
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.activate_account_confirmation
import monuver.feature.account.generated.resources.active
import monuver.feature.account.generated.resources.balance
import monuver.feature.account.generated.resources.deactivate_account_confirmation
import monuver.feature.account.generated.resources.inactive
import monuver.feature.account.generated.resources.name
import monuver.feature.account.generated.resources.status
import monuver.feature.account.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.adrian.monuver.core.presentation.navigation.Account as AccountRoute

@Composable
fun AccountDetailScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<AccountDetailViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val account by viewModel.account.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()


    account?.let { account ->
        AccountDetailContent(
            account = account,
            result = result,
            onAction = { action ->
                when (action) {
                    is AccountDetailAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is AccountDetailAction.NavigateToEditAccount -> {
                        navController.navigate(AccountRoute.Edit(action.accountId))
                    }
                    is AccountDetailAction.DeactivateAccount -> {
                        viewModel.updateAccountStatus(action.accountId, false)
                    }
                    is AccountDetailAction.ActivateAccount -> {
                        viewModel.updateAccountStatus(action.accountId, true)
                    }
                }
            }
        )
    }
}

@Composable
private fun AccountDetailContent(
    account: Account,
    result: DatabaseResultState,
    onAction: (AccountDetailAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var showDeactivateConfirmationDialog by remember { mutableStateOf(false) }
    var showActivateConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        when {
            result.isActivateAccountSuccess() -> {
                snackbarHostState.showSnackbar("Akun telah diaktifkan kembali")
            }
            result.isDeactivateAccountSuccess() -> {
                snackbarHostState.showSnackbar("Akun telah dinonaktifkan")
            }
        }
    }

    Scaffold(
        topBar = {
            AccountDetailAppBar(
                isAccountActive = account.isActive,
                onNavigateBack = {
                    onAction(AccountDetailAction.NavigateBack)
                },
                onNavigateToEditAccount = {
                    onAction(AccountDetailAction.NavigateToEditAccount(account.id))
                },
                onDeactivateAccount = { showDeactivateConfirmationDialog = true },
                onActivateAccount = { showActivateConfirmationDialog = true }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailDataContent(
                title = stringResource(Res.string.name),
                content = account.name
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.type),
                content = stringResource(DatabaseCodeMapper.toAccountType(account.type))
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.balance),
                content = account.balance.toRupiah()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.status),
                content = if (account.isActive) stringResource(Res.string.active) else
                    stringResource(Res.string.inactive)
            )
        }
    }

    if (showDeactivateConfirmationDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.deactivate_account_confirmation),
            onDismissRequest = { showDeactivateConfirmationDialog = false },
            onConfirmRequest = {
                onAction(AccountDetailAction.DeactivateAccount(account.id))
                showDeactivateConfirmationDialog = false
            }
        )
    }

    if (showActivateConfirmationDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.activate_account_confirmation),
            onDismissRequest = { showActivateConfirmationDialog = false },
            onConfirmRequest = {
                onAction(AccountDetailAction.ActivateAccount(account.id))
                showActivateConfirmationDialog = false
            }
        )
    }
}