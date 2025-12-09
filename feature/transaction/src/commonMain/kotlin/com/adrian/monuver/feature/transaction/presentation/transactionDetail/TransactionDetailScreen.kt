package com.adrian.monuver.feature.transaction.presentation.transactionDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.core.presentation.components.ConfirmationDialog
import com.adrian.monuver.core.presentation.components.DetailDataContent
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import com.adrian.monuver.feature.transaction.presentation.transactionDetail.components.TransactionDetailAppBar
import monuver.feature.transaction.generated.resources.Res
import monuver.feature.transaction.generated.resources.amount
import monuver.feature.transaction.generated.resources.category
import monuver.feature.transaction.generated.resources.date
import monuver.feature.transaction.generated.resources.delete_this_transaction
import monuver.feature.transaction.generated.resources.destination_account
import monuver.feature.transaction.generated.resources.source_account
import monuver.feature.transaction.generated.resources.sub_category
import monuver.feature.transaction.generated.resources.title
import monuver.feature.transaction.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.adrian.monuver.core.presentation.navigation.Transaction as TransactionRoute

@Composable
fun TransactionDetailScreen(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = koinViewModel<TransactionDetailViewModel>(
        viewModelStoreOwner = navBackStackEntry,
        parameters = { parametersOf(navBackStackEntry.savedStateHandle) }
    )
    val transaction by viewModel.transaction.collectAsStateWithLifecycle()

    transaction?.let { transaction ->
        TransactionDetailContent(
            transaction = transaction,
            onAction = { action ->
                when (action) {
                    is TransactionDetailAction.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is TransactionDetailAction.NavigateToEditTransaction -> {
                        navController.navigate(TransactionRoute.Edit(action.id))
                    }
                    is TransactionDetailAction.RemoveCurrentTransaction -> {
                        viewModel.deleteTransaction(action.transaction)
                    }
                }
            }
        )
    }
}

@Composable
private fun TransactionDetailContent(
    transaction: Transaction,
    onAction: (TransactionDetailAction) -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TransactionDetailAppBar(
                isTransactionLocked = transaction.isLocked,
                onNavigateBack = {
                    onAction(TransactionDetailAction.NavigateBack)
                },
                onNavigateToEditTransaction = {
                    onAction(TransactionDetailAction.NavigateToEditTransaction(transaction.id))
                },
                onRemoveTransaction = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailDataContent(
                title = stringResource(Res.string.title),
                content = transaction.title
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.type),
                content = stringResource(DatabaseCodeMapper.toTransactionType(transaction.type))
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.category),
                content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(transaction.parentCategory))
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.sub_category),
                content = stringResource(DatabaseCodeMapper.toChildCategoryTitle(transaction.childCategory))
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.date),
                content = DateHelper.formatToReadable(transaction.date)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = stringResource(Res.string.amount),
                content = transaction.amount.toRupiah()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailDataContent(
                title = if (transaction.type == TransactionType.INCOME)
                    stringResource(Res.string.destination_account) else
                        stringResource(Res.string.source_account),
                content = transaction.sourceName
            )
            if (transaction.type == TransactionType.TRANSFER) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DetailDataContent(
                    title = stringResource(Res.string.destination_account),
                    content = transaction.destinationName.toString()
                )
            }
        }
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            label = stringResource(Res.string.delete_this_transaction),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                onAction(TransactionDetailAction.RemoveCurrentTransaction(transaction))
                onAction(TransactionDetailAction.NavigateBack)
            }
        )
    }
}