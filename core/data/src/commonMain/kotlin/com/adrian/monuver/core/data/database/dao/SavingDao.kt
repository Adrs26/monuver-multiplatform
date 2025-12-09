package com.adrian.monuver.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adrian.monuver.core.data.database.entity.room.SavingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingDao {

    @Query("SELECT * FROM saving WHERE is_active = 1 ORDER BY target_date ASC")
    fun getAllActiveSavings(): Flow<List<SavingEntity>>

    @Query("SELECT * FROM saving WHERE is_active = 0 ORDER BY target_date DESC")
    fun getAllInactiveSavings(): Flow<List<SavingEntity>>

    @Query("SELECT SUM(current_amount) FROM saving WHERE is_active = 1")
    fun getTotalSavingCurrentAmount(): Flow<Long?>

    @Query("SELECT * FROM saving WHERE id = :savingId LIMIT 1")
    fun getSavingById(savingId: Long): Flow<SavingEntity?>

    @Query("SELECT current_amount FROM saving WHERE id = :savingId")
    suspend fun getSavingBalance(savingId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewSaving(saving: SavingEntity)

    @Query("""
        UPDATE saving
        SET current_amount = current_amount + :delta 
        WHERE id = :savingId
    """)
    suspend fun increaseSavingCurrentAmount(savingId: Long, delta: Long)

    @Query("""
        UPDATE saving
        SET current_amount = current_amount - :delta 
        WHERE id = :savingId
    """)
    suspend fun decreaseSavingCurrentAmount(savingId: Long, delta: Long)

    @Update
    suspend fun updateSaving(savingEntity: SavingEntity)

    @Query("UPDATE saving SET is_active = 0 WHERE id = :savingId")
    suspend fun updateSavingStatusToInactiveById(savingId: Long)

    @Query("DELETE FROM saving WHERE id = :savingId")
    suspend fun deleteSavingById(savingId: Long)

    @Query("DELETE FROM saving")
    suspend fun deleteAllSavings()

    @Query("SELECT * FROM saving")
    suspend fun getAllSavings(): List<SavingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSavings(savings: List<SavingEntity>)
}