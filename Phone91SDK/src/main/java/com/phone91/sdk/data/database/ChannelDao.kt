package com.phone91.sdk.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phone91.sdk.model.ChannelObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ChannelDao {

    /**
     * Get a user by id.
     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM Channel_table WHERE channel = :channel")
    fun getNAmeByChannel(channel: String): Single<ChannelObject>

    @Query("SELECT * FROM Channel_table WHERE team_id = :id")
    fun getDetailById(id: String): Single<ChannelObject>

    @Query("SELECT count(*) FROM Channel_table where team_id = :id")
    fun isFavouriteShow(id: Long): Int

    /**
     * Insert a user in the database. If the user already exists, replace it.
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChannel(channel: ChannelObject): Completable

    /**
     * Delete all users.
     */
    @Query("DELETE FROM Channel_table")
    fun deleteAllChannel()
}