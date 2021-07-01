package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}

//{"id":1587385161501,"content":"cccc","sender":"richa","type":"chat","attachment_url":"","mime_type":""}
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatObject(var sender: String? = null,
                      var type: String? = null,
                      var content: String?  = null,
                      var id: String? = null,
                      var attachment_url: String?=null,
                      var mime_type: String?=null,
                      var notify: String?=null,
                       var pn_gcm:PnGcmObject?=null,
                      var time:Long=-1
                         ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(PnGcmObject::class.java.classLoader),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sender)
        parcel.writeString(type)
        parcel.writeString(content)
        parcel.writeString(id)
        parcel.writeString(attachment_url)
        parcel.writeString(mime_type)
        parcel.writeString(notify)
        parcel.writeParcelable(pn_gcm, flags)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatObject> {
        override fun createFromParcel(parcel: Parcel): ChatObject {
            return ChatObject(parcel)
        }

        override fun newArray(size: Int): Array<ChatObject?> {
            return arrayOfNulls(size)
        }
    }

}