package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

data class Channel(
    var id: String? = null,
    var channel: String? = null,
    var last_read: String? = null,
    var team_id: String? = null,
    var is_closed: Boolean? = null,
    var assigned_to: Assigns? = null,
    var showCallBtn: Boolean = false,
    var uuid: String? = null,
    var unread_count: String? = null
) : Parcelable
{


    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readParcelable<Assigns>(Assigns::class.java.classLoader),
        1 == source.readInt(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(channel)
        writeString(last_read)
        writeString(team_id)
        writeValue(is_closed)
        writeParcelable(assigned_to, 0)
        writeInt((if (showCallBtn) 1 else 0))
        writeString(uuid)
        writeString(unread_count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Channel> = object : Parcelable.Creator<Channel> {
            override fun createFromParcel(source: Parcel): Channel = Channel(source)
            override fun newArray(size: Int): Array<Channel?> = arrayOfNulls(size)
        }
    }
}