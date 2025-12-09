package com.adrian.monuver.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adrian.monuver.core.data.database.entity.projection.TransactionCategorySummaryEntity
import com.adrian.monuver.core.data.database.entity.projection.TransactionSummaryEntity
import com.adrian.monuver.core.data.database.entity.room.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("""
        SELECT *
        FROM `transaction`
        ORDER BY date DESC, timeStamp DESC
        LIMIT 3
    """)
    fun getRecentTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE (:type IS NULL OR type = :type)
            AND (:month IS NULL OR month = :month)
            AND (:year IS NULL OR year = :year)
            AND (title LIKE '%' || :query || '%')
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?
    ): PagingSource<Int, TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE id = :transactionId LIMIT 1")
    fun getTransactionById(transactionId: Long): Flow<TransactionEntity?>

    @Query("""
        SELECT * 
        FROM `transaction` 
        WHERE save_id = :savingId
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE parent_category = :category AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE parent_category = :category AND month = :month AND year = :year
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionEntity>>

    @Query("SELECT DISTINCT year FROM `transaction` ORDER BY year DESC")
    fun getDistinctTransactionYears(): Flow<List<Int>>

    @Query("""
        SELECT SUM(amount) 
        FROM `transaction`
        WHERE type = :type 
            AND month = :month 
            AND year = :year
    """)
    fun getTotalMonthlyTransactionAmount(type: Int, month: Int, year: Int): Flow<Long?>

    @Query("""
        WITH all_dates AS (
            SELECT DISTINCT DATE(date) as date
            FROM `transaction`
            WHERE strftime('%m', date) = printf('%02d', :month)
                AND strftime('%Y', date) = CAST(:year AS TEXT)
        ),
        daily_totals AS (
            SELECT 
                ad.date,
                COALESCE(SUM(t.amount), 0) as dailyTotal
            FROM all_dates ad
            LEFT JOIN `transaction` t ON DATE(t.date) = ad.date 
                AND t.type = :type AND t.month = :month AND t.year = :year
            GROUP BY ad.date
        )
        SELECT AVG(dailyTotal) FROM daily_totals
    """)
    fun getAverageDailyTransactionAmountInMonth(type: Int, month: Int, year: Int): Flow<Double?>

    @Query("""
        SELECT parent_category, SUM(amount) AS total_amount
        FROM `transaction`
        WHERE type = :type AND month = :month AND year = :year
        GROUP BY parent_category
        ORDER BY total_amount DESC
    """)
    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryEntity>>

    @Query("""
        SELECT type, date, amount FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
    """)
    fun getTransactionsInRange(startDate: String, endDate: String): Flow<List<TransactionSummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewTransaction(transaction: TransactionEntity): Long

    @Query("DELETE FROM `transaction` WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long): Int

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    @Query("""
        SELECT IFNULL(SUM(amount), 0) 
        FROM `transaction`
        WHERE parent_category = :category AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long

    @Query("""
        UPDATE `transaction`
        SET source_name = :accountName
        WHERE source_id = :accountId AND (type = 1001 OR type = 1002)
    """)
    suspend fun updateAccountNameOnCommonTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET 
            source_name = CASE WHEN source_id = :accountId THEN :accountName ELSE source_name END,
            destination_name = CASE WHEN destination_id = :accountId THEN :accountName ELSE destination_name END
        WHERE (source_id = :accountId OR destination_id = :accountId) AND type = 1003 AND child_category = 1003
    """)
    suspend fun updateAccountNameOnTransferTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET source_name = :accountName
        WHERE source_id = :accountId AND type = 1003 AND child_category = 1004
    """)
    suspend fun updateAccountNameOnDepositTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET destination_name = :accountName
        WHERE destination_id = :accountId AND type = 1003 AND child_category = 1005
    """)
    suspend fun updateAccountNameOnWithdrawTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET destination_name = :savingTitle
        WHERE save_id = :savingId AND type = 1003 AND child_category = 1004
    """)
    suspend fun updateSavingTitleOnDepositTransaction(savingId: Long, savingTitle: String)

    @Query("""
        UPDATE `transaction`
        SET source_name = :savingTitle
        WHERE save_id = :savingId AND type = 1003 AND child_category = 1005
    """)
    suspend fun updateSavingTitleOnWithdrawTransaction(savingId: Long, savingTitle: String)

    @Query("""
        SELECT * 
        FROM `transaction` 
        WHERE save_id = :savingId
        ORDER BY date DESC, timeStamp DESC
    """)
    suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionEntity>

    @Query("""
        UPDATE `transaction`
        SET is_locked = :isLocked
        WHERE (type = 1001 OR type = 1002) AND is_special_case = 0 AND source_id = :accountId
    """)
    suspend fun updateTransactionLockStatusByAccountId(accountId: Int, isLocked: Boolean)

    @Query("SELECT * FROM `transaction` WHERE bill_id = :billId LIMIT 1")
    suspend fun getTransactionIdByBillId(billId: Long): TransactionEntity?

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAllTransactions()

    @Query("SELECT * FROM `transaction`")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransactions(transactions: List<TransactionEntity>)

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date ASC
    """)
    suspend fun getTransactionsInRangeByDateAsc(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    suspend fun getTransactionsInRangeByDateDesc(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY type ASC, date ASC
    """)
    suspend fun getTransactionsInRangeByDateAscWithType(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY type ASC, date DESC
    """)
    suspend fun getTransactionsInRangeByDateDescWithType(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT SUM(amount) 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        AND type = 1001
    """)
    suspend fun getTotalIncomeTransactionsInRange(startDate: String, endDate: String): Long?

    @Query("""
        SELECT SUM(amount) 
        FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
        AND type = 1002
    """)
    suspend fun getTotalExpenseTransactionsInRange(startDate: String, endDate: String): Long?
}