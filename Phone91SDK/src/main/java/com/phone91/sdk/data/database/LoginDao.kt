package com.phone91.sdk.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phone91.sdk.model.ChannelObject
import com.phone91.sdk.model.LoginObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface LoginDao {

    /**
     * Get a user by id.
     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM login_table")
    fun getLoginDetail(): Single<LoginObject>

    /**
     * Insert a user in the database. If the user already exists, replace it.
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChannel(loginObject: LoginObject): Completable

    /**
     * Delete all users.
     */
    @Query("DELETE FROM login_table")
    fun deleteAllLogin(): Completable
}