package com.phone91.sdk.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phone91.sdk.model.ChannelObject
import com.phone91.sdk.model.LoginObject
import com.phone91.sdk.model.WidgetDBObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface WidgetDao {

    /**
     * Get a user by id.
     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM widget_table WHERE" +
            " widgetToken = :widgetToken")
    fun getWidgetDetail(widgetToken: String): Single<WidgetDBObject>

    /**
     * Insert a user in the database. If the user already exists, replace it.
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWidget(widgetDBObject: WidgetDBObject): Completable

    /**
     * Delete all users.
     */
    @Query("DELETE FROM widget_table")
    fun deleteAllWidget(): Completable


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertOrUpdate(widgetDBObject: WidgetDBObject)

//    fun insertOrUpdate(widgetDBObject: WidgetDBObject) {
//        var itemsFromDB = getWidgetDetail(widgetDBObject.widgetToken!!)
//        if (itemsFromDB.)
//            insert(item)
//        else
//            updateQuantity(item.getId())
//    }

}