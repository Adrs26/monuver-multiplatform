package com.adrian.monuver.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adrian.monuver.core.data.database.entity.room.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bill WHERE due_date > date('now') AND is_paid = 0 ORDER BY due_date ASC, time_stamp DESC")
    fun getPendingBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill WHERE due_date <= date('now') AND is_paid = 0 ORDER BY due_date DESC, time_stamp DESC")
    fun getDueBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill WHERE is_paid = 1 ORDER BY paid_date DESC, time_stamp DESC")
    fun getPaidBills(): PagingSource<Int, BillEntity>

    @Query("SELECT * FROM bill WHERE id = :billId LIMIT 1")
    fun getBillById(billId: Long): Flow<BillEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBill(bill: BillEntity): Long

    @Query("UPDATE bill SET parent_id = :parentId WHERE id = :id")
    suspend fun updateParentId(id: Long, parentId: Long)

    @Query("UPDATE bill SET is_paid = :isPaid, paid_date = :paidDate, is_paid_before = 1 WHERE id = :billId")
    suspend fun updateBillPaidStatusById(billId: Long, paidDate: String?, isPaid: Boolean)

    @Query("DELETE FROM bill WHERE id = :billId")
    suspend fun deleteBillById(billId: Long)

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Query("UPDATE bill set period = :period, fix_period = :fixPeriod WHERE parent_id = :parentId")
    suspend fun updateBillPeriodByParentId(period: Int?, fixPeriod: Int?, parentId: Long)

    @Query("DELETE FROM bill")
    suspend fun deleteAllBills()

    @Query("SELECT * FROM bill")
    suspend fun getAllBills(): List<BillEntity>

    @Query("SELECT * FROM bill WHERE is_paid = 0")
    suspend fun getAllUnpaidBills(): List<BillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBills(bills: List<BillEntity>)
}