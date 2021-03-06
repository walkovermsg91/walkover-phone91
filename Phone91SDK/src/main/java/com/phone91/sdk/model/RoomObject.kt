package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import org.json.JSONObject

//{"status":"Success","message":"Client updated",
// "body":{"param_1":"Tipu",
// "uuid":"87ea0bfd-2e3c-49c1-a204-44f9a1e0e6fc",
// "param_2":"t2@sultan.com","param_3":"t2@sultan.com",
// "unique_id":"5d1a7cd1-bfe5-4d56-8ef5-c31543999134","city":"Indore",
// "region":"Madhya Pradesh","country":"India","continent":"Asia"}}://testapi.phone91.com/client/}
data class RoomObject(
    var jwt: String? = null,
    var room: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(jwt)
        writeString(room)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RoomObject> = object : Parcelable.Creator<RoomObject> {
            override fun createFromParcel(source: Parcel): RoomObject = RoomObject(source)
            override fun newArray(size: Int): Array<RoomObject?> = arrayOfNulls(size)
        }
    }
}