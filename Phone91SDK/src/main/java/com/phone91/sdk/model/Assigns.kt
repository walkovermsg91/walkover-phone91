package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

data class Assigns(
    var name: String? = null,
    var username: String? = null
) : Parcelable
{
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(username)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Assigns> = object : Parcelable.Creator<Assigns> {
            override fun createFromParcel(source: Parcel): Assigns = Assigns(source)
            override fun newArray(size: Int): Array<Assigns?> = arrayOfNulls(size)
        }
    }
}