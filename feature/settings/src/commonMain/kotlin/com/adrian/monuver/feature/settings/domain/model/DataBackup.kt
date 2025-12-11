package com.adrian.monuver.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DataBackup(
    @SerialName("accounts") val accounts: List<AccountBackup>,
    @SerialName("bills") val bills: List<BillBackup>,
    @SerialName("budgets") val budgets: List<BudgetBackup>,
    @SerialName("savings") val savings: List<SavingBackup>,
    @SerialName("transactions") val transactions: List<TransactionBackup>
)
