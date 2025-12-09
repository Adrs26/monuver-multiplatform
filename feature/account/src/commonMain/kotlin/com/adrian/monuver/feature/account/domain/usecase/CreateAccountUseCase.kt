package com.adrian.monuver.feature.account.domain.usecase

import com.adrian.monuver.core.domain.common.DatabaseResultState
import com.adrian.monuver.core.domain.model.Account
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.feature.account.domain.model.AddAccount
import com.adrian.monuver.feature.account.domain.repository.AccountRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateAccountUseCase(
    private val repository: AccountRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(account: AddAccount): DatabaseResultState {
        when {
            account.name.isEmpty() -> return DatabaseResultState.EmptyAccountName
            account.type == 0 -> return DatabaseResultState.EmptyAccountType
            account.balance == 0L -> return DatabaseResultState.EmptyAccountBalance
        }

        val account = Account(
            name = account.name,
            type = account.type,
            balance = account.balance,
            isActive = true
        )

        val currentDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString()
        val (month, year) = DateHelper.getMonthAndYear(currentDate)

        val transaction = Transaction(
            title = "Penambahan Akun",
            type = TransactionType.INCOME,
            parentCategory = TransactionParentCategory.OTHER_INCOME,
            childCategory = TransactionChildCategory.OTHER_INCOME,
            date = currentDate,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = account.balance,
            sourceId = 0,
            sourceName = account.name,
            isLocked = true,
            isSpecialCase = true
        )

        repository.createAccount(
            account = account,
            transaction = transaction
        )
        return DatabaseResultState.CreateAccountSuccess
    }
}