package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//{"id":104163,"channel":"ch-user-32.4f49ec6157494a93a56923984a4241e4",
////                        "uuid":"06f32e87-2bb5-4a9e-99fb-09db27c7b555","last_read":null,"team_id":null,"name":"Lime Lobster","number":null,"mail":null,"unique_id":null,
////                        "assigned_to":null,"is_closed":false}

@Entity(tableName = "widget_table")

data class WidgetDBObject (/*@PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "id") var id: Int?,*/
                           @PrimaryKey() @ColumnInfo(name = "widgetToken") var widgetToken: String ,
                           @ColumnInfo(name = "uniqueId") var uniqueId: String?  = null,
                           @ColumnInfo(name = "uuid") var uuid : String? =null
                         )  {

//    constructor(parcel: Parcel) : this(
////        parcel.readInt(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString()
//
//
//    ) {
//    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
////        parcel.writeInt(id!!)
//        parcel.writeString(widgetToken)
//        parcel.writeString(uniqueId)
//        parcel.writeString(uuid)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<WidgetDBObject> {
//        override fun createFromParcel(parcel: Parcel): WidgetDBObject {
//            return WidgetDBObject(parcel)
//        }
//
//        override fun newArray(size: Int): Array<WidgetDBObject?> {
//            return arrayOfNulls(size)
//        }
//    }

}