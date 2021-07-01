package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}

//{"id":1587385161501,"content":"cccc","sender":"richa","type":"chat","attachment_url":"","mime_type":""}
data class DataObject(var type: String? = null,
                      var notify: String?=null
                      ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(notify)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataObject> {
        override fun createFromParcel(parcel: Parcel): DataObject {
            return DataObject(parcel)
        }

        override fun newArray(size: Int): Array<DataObject?> {
            return arrayOfNulls(size)
        }
    }

}