package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//{"name": "agent Name", "username": "agent username for presence checks"}
data class AgentObject(var name: String? = null,
                       var username: String? = null
                         ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AgentObject> {
        override fun createFromParcel(parcel: Parcel): AgentObject {
            return AgentObject(parcel)
        }

        override fun newArray(size: Int): Array<AgentObject?> {
            return arrayOfNulls(size)
        }
    }


}