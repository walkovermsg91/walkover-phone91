package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//"teams":[{"id":93,"name":"Teating"}]
data class TeamObject(
    var id: String? = null,
    var name: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(

        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TeamObject> = object : Parcelable.Creator<TeamObject> {
            override fun createFromParcel(source: Parcel): TeamObject = TeamObject(source)
            override fun newArray(size: Int): Array<TeamObject?> = arrayOfNulls(size)
        }
    }
}