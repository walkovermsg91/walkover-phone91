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

@Entity(tableName = "Channel_table")

data class ChannelObject (@PrimaryKey(autoGenerate = false)  @ColumnInfo(name = "id") var id: Int,
//                          @ColumnInfo(name = "id") var id: String? = null,
                         @ColumnInfo(name = "channel") var channel: String? = null,
                         @ColumnInfo(name = "uuid") var uuid: String?  = null,
                         @ColumnInfo(name = "last_read") var last_read: String?  = null,
                         @ColumnInfo(name = "team_id") var team_id: String?  = null,
                         @ColumnInfo(name = "name") var name: String?  = null,
                         @ColumnInfo(name = "number") var number : String? =null,
                         @ColumnInfo(name = "mail") var mail : String? =null,
                         @ColumnInfo(name = "unique_id") var unique_id : String? =null,
                         @ColumnInfo(name = "call_enabled") var call_enabled : Boolean =false,
                         @ColumnInfo(name = "country") var country : String? =null,
                         @ColumnInfo(name = "presence_channel") var presence_channel : String? =null,
                         @ColumnInfo(name = "unread_count") var unread_count : String? =null


                         ) : Parcelable {
    @Ignore var is_closed : Boolean=false
    @Ignore var assigned_to : AgentObject?=null

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
//        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
        /*parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readParcelable(AgentObject::class.java.classLoader)*/
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
//        parcel.writeString(id)
        parcel.writeString(channel)
        parcel.writeString(uuid)
        parcel.writeString(last_read)
        parcel.writeString(team_id)
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeString(mail)
        parcel.writeString(unique_id)
        parcel.writeValue(is_closed)
        parcel.writeParcelable(assigned_to, flags)
        parcel.writeByte(if (call_enabled) 1 else 0)
        parcel.writeString(country)
        parcel.writeString(presence_channel)
        parcel.writeString(unread_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelObject> {
        override fun createFromParcel(parcel: Parcel): ChannelObject {
            return ChannelObject(parcel)
        }

        override fun newArray(size: Int): Array<ChannelObject?> {
            return arrayOfNulls(size)
        }
    }

}