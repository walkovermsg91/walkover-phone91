package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson


/**
 * Created by richa WALK311 on 09/04/2020.
 */

//{"id":104163,"channel":"ch-user-32.4f49ec6157494a93a56923984a4241e4",
////                        "uuid":"06f32e87-2bb5-4a9e-99fb-09db27c7b555","last_read":null,"team_id":null,"name":"Lime Lobster","number":null,"mail":null,"unique_id":null,
////                        "assigned_to":null,"is_closed":false}


data class ChannelListObject(
    var name: String? = null,
    var number: String? = null,
    var mail: String? = null,
    var unique_id: String? = null,
    var uuid: String? = null,
    var channel: String? = null,
    var call_enabled: Boolean? = null,
    var channels: ArrayList<Channel>? = null,
    var country: String? = null,
    var presence_channel: String? = null
) : Parcelable {


    override fun toString(): String {
        return Gson().toJson(this)
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.createTypedArrayList(Channel.CREATOR),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(number)
        writeString(mail)
        writeString(unique_id)
        writeString(uuid)
        writeString(channel)
        writeValue(call_enabled)
        writeTypedList(channels)
        writeString(country)
        writeString(presence_channel)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ChannelListObject> =
            object : Parcelable.Creator<ChannelListObject> {
                override fun createFromParcel(source: Parcel): ChannelListObject =
                    ChannelListObject(source)

                override fun newArray(size: Int): Array<ChannelListObject?> = arrayOfNulls(size)
            }
    }
}