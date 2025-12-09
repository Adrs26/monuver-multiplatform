package com.adrian.monuver.core.presentation.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.model.TransactionItem
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.active_budgeting_with_category_already_exists
import monuver.core.presentation.generated.resources.all
import monuver.core.presentation.generated.resources.budget_end_date_cannot_be_before_today
import monuver.core.presentation.generated.resources.budget_start_date_cannot_be_after_today
import monuver.core.presentation.generated.resources.current_budget_amount_exceeds_maximum_limit
import monuver.core.presentation.generated.resources.empty_account_balance
import monuver.core.presentation.generated.resources.empty_account_name
import monuver.core.presentation.generated.resources.empty_account_type
import monuver.core.presentation.generated.resources.empty_bill_amount
import monuver.core.presentation.generated.resources.empty_bill_date
import monuver.core.presentation.generated.resources.empty_bill_fix_period
import monuver.core.presentation.generated.resources.empty_bill_title
import monuver.core.presentation.generated.resources.empty_budgeting_category
import monuver.core.presentation.generated.resources.empty_budgeting_max_amount
import monuver.core.presentation.generated.resources.empty_deposit_withdraw_amount
import monuver.core.presentation.generated.resources.empty_deposit_withdraw_date
import monuver.core.presentation.generated.resources.empty_report_end_date
import monuver.core.presentation.generated.resources.empty_report_start_date
import monuver.core.presentation.generated.resources.empty_report_title
import monuver.core.presentation.generated.resources.empty_report_username
import monuver.core.presentation.generated.resources.empty_save_amount
import monuver.core.presentation.generated.resources.empty_save_target_amount
import monuver.core.presentation.generated.resources.empty_save_target_date
import monuver.core.presentation.generated.resources.empty_save_title
import monuver.core.presentation.generated.resources.empty_transaction_amount
import monuver.core.presentation.generated.resources.empty_transaction_category
import monuver.core.presentation.generated.resources.empty_transaction_date
import monuver.core.presentation.generated.resources.empty_transaction_destination
import monuver.core.presentation.generated.resources.empty_transaction_source
import monuver.core.presentation.generated.resources.empty_transaction_title
import monuver.core.presentation.generated.resources.insufficient_account_balance
import monuver.core.presentation.generated.resources.insufficient_saving_balance
import monuver.core.presentation.generated.resources.invalid_bill_fix_period
import monuver.core.presentation.generated.resources.saving_target_date_cannot_be_before_today
import monuver.core.presentation.generated.resources.transaction_date_cannot_be_after_today
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersHolder
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 700L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return this.then(
        Modifier.clickable {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

inline fun <reified T : Any> NavGraphBuilder.animatedComposable(
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() },
        content = content
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavHostController,
    noinline parameters: (() -> ParametersHolder)? = null
): T {
    val navGraphRoute = destination.parent?.route ?: error("No parent route")
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return if (parameters != null) {
        koinViewModel(
            viewModelStoreOwner = parentEntry,
            parameters = parameters
        )
    } else {
        koinViewModel(
            viewModelStoreOwner = parentEntry
        )
    }
}

fun Transaction.toItem() = TransactionItem(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    amount = amount,
    sourceName = sourceName,
    isLocked = isLocked
)

fun DatabaseResultState.toStringRes() = when (this) {
    is DatabaseResultState.EmptyAccountBalance -> Res.string.empty_account_balance
    is DatabaseResultState.EmptyAccountName -> Res.string.empty_account_name
    is DatabaseResultState.EmptyAccountType -> Res.string.empty_account_type
    is DatabaseResultState.InsufficientAccountBalance -> Res.string.insufficient_account_balance
    is DatabaseResultState.ActiveBudgetWithCategoryAlreadyExists -> Res.string.active_budgeting_with_category_already_exists
    is DatabaseResultState.BudgetStartDateAfterToday -> Res.string.budget_start_date_cannot_be_after_today
    is DatabaseResultState.BudgetEndDateBeforeToday -> Res.string.budget_end_date_cannot_be_before_today
    is DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit -> Res.string.current_budget_amount_exceeds_maximum_limit
    is DatabaseResultState.EmptyBillAmount -> Res.string.empty_bill_amount
    is DatabaseResultState.EmptyBillDate -> Res.string.empty_bill_date
    is DatabaseResultState.EmptyBillFixPeriod -> Res.string.empty_bill_fix_period
    is DatabaseResultState.EmptyBillTitle -> Res.string.empty_bill_title
    is DatabaseResultState.EmptyBudgetCategory -> Res.string.empty_budgeting_category
    is DatabaseResultState.EmptyBudgetMaxAmount -> Res.string.empty_budgeting_max_amount
    is DatabaseResultState.EmptyDepositAmount -> Res.string.empty_deposit_withdraw_amount
    is DatabaseResultState.EmptyDepositDate -> Res.string.empty_deposit_withdraw_date
    is DatabaseResultState.EmptyReportEndDate -> Res.string.empty_report_end_date
    is DatabaseResultState.EmptyReportStartDate -> Res.string.empty_report_start_date
    is DatabaseResultState.EmptyReportTitle -> Res.string.empty_report_title
    is DatabaseResultState.EmptyReportUsername -> Res.string.empty_report_username
    is DatabaseResultState.EmptySavingAmount -> Res.string.empty_save_amount
    is DatabaseResultState.EmptySavingTargetAmount -> Res.string.empty_save_target_amount
    is DatabaseResultState.EmptySavingTargetDate -> Res.string.empty_save_target_date
    is DatabaseResultState.EmptySavingTitle -> Res.string.empty_save_title
    is DatabaseResultState.EmptyWithdrawAmount -> Res.string.empty_deposit_withdraw_amount
    is DatabaseResultState.EmptyWithdrawDate -> Res.string.empty_deposit_withdraw_date
    is DatabaseResultState.InsufficientSavingBalance -> Res.string.insufficient_saving_balance
    is DatabaseResultState.InvalidBillFixPeriod -> Res.string.invalid_bill_fix_period
    is DatabaseResultState.EmptyTransactionAmount -> Res.string.empty_transaction_amount
    is DatabaseResultState.EmptyTransactionCategory -> Res.string.empty_transaction_category
    is DatabaseResultState.EmptyTransactionDate -> Res.string.empty_transaction_date
    is DatabaseResultState.EmptyTransactionDestination -> Res.string.empty_transaction_destination
    is DatabaseResultState.EmptyTransactionSource -> Res.string.empty_transaction_source
    is DatabaseResultState.EmptyTransactionTitle -> Res.string.empty_transaction_title
    is DatabaseResultState.TransactionDateAfterToday -> Res.string.transaction_date_cannot_be_after_today
    is DatabaseResultState.SavingTargetDateBeforeToday -> Res.string.saving_target_date_cannot_be_before_today
    else -> Res.string.all
}
