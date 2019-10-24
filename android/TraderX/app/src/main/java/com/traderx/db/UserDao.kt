package com.traderx.db

import androidx.room.*
import io.reactivex.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Query("DELETE FROM user")
    fun deleteUser(): Completable
}