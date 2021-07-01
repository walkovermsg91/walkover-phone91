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

@Entity(tableName = "login_table")

data class LoginObject (@PrimaryKey(autoGenerate = false)  @ColumnInfo(name = "id") var id: Int,
                        @ColumnInfo(name = "name") var name: String?  = null,
                        @ColumnInfo(name = "number") var number : String? =null,
                        @ColumnInfo(name = "mail") var mail : String? =null
                         ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeString(mail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginObject> {
        override fun createFromParcel(parcel: Parcel): LoginObject {
            return LoginObject(parcel)
        }

        override fun newArray(size: Int): Array<LoginObject?> {
            return arrayOfNulls(size)
        }
    }

}