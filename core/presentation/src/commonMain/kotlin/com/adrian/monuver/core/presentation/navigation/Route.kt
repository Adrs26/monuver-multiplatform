package com.adrian.monuver.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable class Starting {
    @Serializable data object Onboarding
    @Serializable data object CheckAppVersion
    @Serializable data object Main
}

@Serializable class Main {
    @Serializable data object Home
    @Serializable data object Transaction
    @Serializable data object Budgeting
    @Serializable data object Analytics
}

@Serializable class Transaction {
    @Serializable data class  Add(val type: Int = 0)
    @Serializable data class  AddCategory(val type: Int = 0)
    @Serializable data object AddAccount
    @Serializable data object Transfer
    @Serializable data class  TransferAccount(val type: Int = 0)
    @Serializable data class  Detail(val id: Long = 0)
    @Serializable data class  Edit(val id: Long = 0)
    @Serializable data class  EditCategory(val type: Int = 0)
}

@Serializable class Budget {
    @Serializable data object Add
    @Serializable data object AddCategory
    @Serializable data class  Detail(val id: Long = 0)
    @Serializable data class  Edit(val id: Long = 0)
    @Serializable data object Inactive
}

@Serializable class Analytics {
    @Serializable data class Transaction(
        val category: Int = 0,
        val month: Int = 0,
        val year: Int = 0
    )
}

@Serializable class Account {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data object AddType
    @Serializable data class  Detail(val id: Int = 0)
    @Serializable data class  Edit(val id: Int = 0)
    @Serializable data object EditType
}

@Serializable class Billing {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data class  Detail(val id: Long = 0)
    @Serializable data class  Edit(val id: Long = 0)
    @Serializable data class  Payment(val id: Long = 0)
    @Serializable data object PaymentCategory
    @Serializable data object PaymentAccount
}

@Serializable class Settings {
    @Serializable data object Main
    @Serializable data object Export
}

@Serializable class Saving {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data class  Detail(val id: Long = 0)
    @Serializable data class  Edit(val id: Long = 0)
    @Serializable data class  Deposit(val id: Long? = null, val name: String? = null)
    @Serializable data object DepositAccount
    @Serializable data class  Withdraw(val id: Long? = null, val name: String? = null)
    @Serializable data object WithdrawAccount
    @Serializable data object Inactive
}