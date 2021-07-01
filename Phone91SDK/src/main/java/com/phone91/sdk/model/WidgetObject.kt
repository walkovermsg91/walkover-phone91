package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//{"name":"Android","tagline":"SDK testing","teams":[{"id":93,"name":"Teating"}],"hide_launcher":false,"show_send_button":true,"show_close_button":true,"auto_focus":true}
//{"name":"Android","tagline":"SDK testing","teams":[{"id":95,"name":"support"},{"id":93,"name":"Testing"}],"hide_launcher":false,"show_send_button":true,"show_close_button":true,
//    "auto_focus":true,"enable_call":true,"enable_faq":false}
data class WidgetObject(
    var name: String? = null,
    var tagline: String? = null,
    var teams: ArrayList<TeamObject>? = null,
    var hide_launcher: Boolean = false,
    var show_send_button: Boolean = false,
    var show_close_button: Boolean = false,
    var auto_focus: Boolean = false,
    var enable_call: Boolean = false,
    var enable_faq: Boolean = false

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.createTypedArrayList(TeamObject.CREATOR),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(tagline)
        writeTypedList(teams)
        writeInt((if (hide_launcher) 1 else 0))
        writeInt((if (show_send_button) 1 else 0))
        writeInt((if (show_close_button) 1 else 0))
        writeInt((if (auto_focus) 1 else 0))
        writeInt((if (enable_call) 1 else 0))
        writeInt((if (enable_faq) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WidgetObject> = object : Parcelable.Creator<WidgetObject> {
            override fun createFromParcel(source: Parcel): WidgetObject = WidgetObject(source)
            override fun newArray(size: Int): Array<WidgetObject?> = arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}