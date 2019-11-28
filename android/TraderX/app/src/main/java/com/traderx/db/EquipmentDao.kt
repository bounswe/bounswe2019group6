package com.traderx.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment where id = :equipmentId")
    fun getEquipment(equipmentId: Int): Single<Equipment>

    @Query("SELECT * FROM equipment LIMIT 10")
    fun getEquipments(): Flowable<List<Equipment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(equipment: Equipment): Completable

    @Query("DELETE FROM equipment where id = :equipmentId")
    fun deleteEquipment(equipmentId: Int): Completable
}