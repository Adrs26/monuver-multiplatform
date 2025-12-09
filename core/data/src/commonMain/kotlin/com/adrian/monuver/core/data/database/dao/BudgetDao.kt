package com.adrian.monuver.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adrian.monuver.core.data.database.entity.room.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget WHERE is_active = 1 ORDER BY end_date ASC")
    fun getAllActiveBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budget WHERE is_active = 0 ORDER BY end_date DESC")
    fun getAllInactiveBudgets(): PagingSource<Int, BudgetEntity>

    @Query("SELECT * FROM budget WHERE id = :budgetId LIMIT 1")
    fun getBudgetById(budgetId: Long): Flow<BudgetEntity?>

    @Query("""
        SELECT IFNULL(SUM(max_amount), 0) 
        FROM budget
        WHERE is_active = 1
    """)
    fun getTotalBudgetMaxAmount(): Flow<Long>

    @Query("""
        SELECT IFNULL(SUM(used_amount), 0) 
        FROM budget
        WHERE is_active = 1
    """)
    fun getTotalBudgetUsedAmount(): Flow<Long>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM budget
            WHERE category = :category AND is_active = 1
        )
    """)
    suspend fun isBudgetExists(category: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBudget(budget: BudgetEntity): Long

    @Query("""
        SELECT * FROM budget 
        WHERE category = :category AND is_active = 1
        AND :date BETWEEN start_date AND end_date
        LIMIT 1
    """)
    suspend fun getBudgetForDate(category: Int, date: String): BudgetEntity?

    @Query("SELECT (CAST(used_amount AS FLOAT) / CAST(max_amount AS FLOAT)) * 100 FROM budget WHERE category = :category AND is_active = 1")
    suspend fun getBudgetUsagePercentage(category: Int): Float

    @Query("""
        UPDATE budget
        SET used_amount = used_amount + :delta 
        WHERE category = :category
        AND :date BETWEEN start_date AND end_date
    """)
    suspend fun increaseBudgetUsedAmount(category: Int, date: String, delta: Long)

    @Query("""
        UPDATE budget 
        SET used_amount = used_amount - :delta 
        WHERE category = :category
        AND :date BETWEEN start_date AND end_date
    """)
    suspend fun decreaseBudgetUsedAmount(category: Int, date: String, delta: Long)

    @Query("UPDATE budget SET is_active = 0 WHERE category = :category")
    suspend fun updateBudgetStatusToInactive(category: Int)

    @Query("DELETE FROM budget WHERE id = :budgetId")
    suspend fun deleteBudgetById(budgetId: Long)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudgets()

    @Query("SELECT * FROM budget")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBudgets(budgets: List<BudgetEntity>)
}