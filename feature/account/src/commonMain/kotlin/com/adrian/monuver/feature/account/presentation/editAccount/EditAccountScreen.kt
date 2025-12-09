package com.adrian.monuver.feature.account.presentation.editAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.util.isEmptyAccountName
import com.adrian.monuver.core.domain.util.isEmptyAccountType
import com.adrian.monuver.core.domain.util.isUpdateAccountSuccess
import com.adrian.monuver.core.domain.util.toFormattedAmount
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.DisableTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.account.domain.model.EditAccount
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.balance
import monuver.feature.account.generated.resources.choose_account_type
import monuver.feature.account.generated.resources.edit_account
import monuver.feature.account.generated.resources.enter_account_name
import monuver.feature.account.generated.resources.name
import monuver.feature.account.generated.resources.save
import monuver.feature.account.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import com.adrian.monuver.core.presentation.navigation.Account as AccountRoute

@Composable
fun EditAccountScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<EditAccountViewModel>(navController) {
        parametersOf(navBackStackEntry.savedStateHandle)
    }
    val account by viewModel.account.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()

    account?.let { account ->
        EditAccountContent(
            account = account,
            result = result,
            onAction = { action ->
                when (action) {
                    is EditAccountAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditAccountAction.NavigateToType -> {
                        navController.navigate(AccountRoute.EditType)
                    }
                    is EditAccountAction.EditCurrentAccount -> {
                        viewModel.updateAccount(action.account)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EditAccountContent(
    account: Account,
    result: DatabaseResultState,
    onAction: (EditAccountAction) -> Unit
) {
    val name = rememberTextFieldState(initialText = account.name)

    LaunchedEffect(result) {
        result.let { result ->
            if (result.isUpdateAccountSuccess()) {
                onAction(EditAccountAction.NavigateBack)
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.edit_account),
                onNavigateBack = {
                    onAction(EditAccountAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.save),
                onClick = {
                    onAction(
                        EditAccountAction.EditCurrentAccount(
                            EditAccount(
                                id = account.id,
                                name = name.text.toString(),
                                type = account.type,
                                balance = account.balance
                            )
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonTextField(
                state = name,
                label = stringResource(Res.string.name),
                placeholder = stringResource(Res.string.enter_account_name),
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isError = result.isEmptyAccountName()
            )
            ClickTextField(
                value = if (account.type == 0) "" else
                    stringResource(DatabaseCodeMapper.toAccountType(account.type)),
                label = stringResource(Res.string.type),
                placeholder = stringResource(Res.string.choose_account_type),
                onClick = {
                    onAction(EditAccountAction.NavigateToType)
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyAccountType()
            )
            DisableTextField(
                label = stringResource(Res.string.balance),
                value = account.balance.toFormattedAmount(),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}