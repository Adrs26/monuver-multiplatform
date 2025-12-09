package com.adrian.monuver.feature.account.presentation.addAccount

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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.util.isCreateAccountSuccess
import com.adrian.monuver.core.domain.util.isEmptyAccountBalance
import com.adrian.monuver.core.domain.util.isEmptyAccountName
import com.adrian.monuver.core.domain.util.isEmptyAccountType
import com.adrian.monuver.core.presentation.components.ClickTextField
import com.adrian.monuver.core.presentation.components.CommonAppBar
import com.adrian.monuver.core.presentation.components.CommonTextField
import com.adrian.monuver.core.presentation.components.NumberTextField
import com.adrian.monuver.core.presentation.components.PrimaryActionButton
import com.adrian.monuver.core.presentation.navigation.Account
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.core.presentation.util.sharedKoinViewModel
import com.adrian.monuver.core.presentation.util.toStringRes
import com.adrian.monuver.feature.account.domain.model.AddAccount
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.add
import monuver.feature.account.generated.resources.add_new_account
import monuver.feature.account.generated.resources.choose_account_type
import monuver.feature.account.generated.resources.enter_account_name
import monuver.feature.account.generated.resources.name
import monuver.feature.account.generated.resources.start_balance
import monuver.feature.account.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddAccountScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = navBackStackEntry.sharedKoinViewModel<AddAccountViewModel>(navController)
    val type by viewModel.accountType.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()

    AddAccountContent(
        type = type,
        result = result,
        onAction = { action ->
            when (action) {
                is AddAccountAction.NavigateBack -> {
                    navController.navigateUp()
                }
                is AddAccountAction.NavigateToType -> {
                    navController.navigate(Account.AddType)
                }
                is AddAccountAction.AddNewAccount -> {
                    viewModel.createNewAccount(action.account)
                }
            }
        }
    )
}

@Composable
private fun AddAccountContent(
    type: Int,
    result: DatabaseResultState,
    onAction: (AddAccountAction) -> Unit
) {
    val name = rememberTextFieldState(initialText = "")
    val formattedBalance = rememberTextFieldState(initialText = "")

    var rawBalance by remember { mutableLongStateOf(0) }

    LaunchedEffect(result) {
        result.let { result ->
            if (result.isCreateAccountSuccess()) {
                onAction(AddAccountAction.NavigateBack)
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(Res.string.add_new_account),
                onNavigateBack = {
                    onAction(AddAccountAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            PrimaryActionButton(
                label = stringResource(Res.string.add),
                onClick = {
                    onAction(
                        AddAccountAction.AddNewAccount(
                            AddAccount(
                                name = name.text.toString(),
                                type = type,
                                balance = rawBalance
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
                value = if (type == 0) "" else stringResource(DatabaseCodeMapper.toAccountType(type)),
                label = stringResource(Res.string.type),
                placeholder = stringResource(Res.string.choose_account_type),
                onClick = {
                    onAction(AddAccountAction.NavigateToType)
                },
                errorMessage = stringResource(result.toStringRes()),
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyAccountType()
            )
            NumberTextField(
                state = formattedBalance,
                label = stringResource(Res.string.start_balance),
                errorMessage = stringResource(result.toStringRes()),
                onValueAsLongCent = { rawBalance = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                isError = result.isEmptyAccountBalance()
            )
        }
    }
}