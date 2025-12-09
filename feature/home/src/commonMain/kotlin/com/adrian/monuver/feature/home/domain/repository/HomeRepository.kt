package com.adrian.monuver.feature.home.domain.repository

import com.adrian.monuver.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

internal interface HomeRepository {

    fun getActiveAccountBalance(): Flow<Long?>

    fun getRecentTransactions(): Flow<List<Transaction>>
}