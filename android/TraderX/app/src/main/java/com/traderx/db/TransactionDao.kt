package com.traderx.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions where id = :transactionId")
    fun getTransaction(transactionId: Int): Single<Transaction>

    @Query("SELECT * FROM transactions LIMIT 10")
    fun getTransactions(): Flowable<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(transaction: Transaction): Completable

    @Query("DELETE FROM transactions where id = :transactionId")
    fun deleteEquipment(transactionId: Int): Completable
}