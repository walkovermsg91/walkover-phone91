package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
//{"status":"Success","message":"Client updated","body":{"param_1":"Tipu","uuid":"87ea0bfd-2e3c-49c1-a204-44f9a1e0e6fc","param_2":"t2@sultan.com","param_3":"t2@sultan.com","unique_id":"5d1a7cd1-bfe5-4d56-8ef5-c31543999134","city":"Indore","region":"Madhya Pradesh","country":"India","continent":"Asia"}}://testapi.phone91.com/client/}
data class ParamObject(
    var id: String? = null,
    var name: String? = null,
    var type: String? = null,
    var description: String? = null,
    var created_at: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(type)
        writeString(description)
        writeString(created_at)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ParamObject> = object : Parcelable.Creator<ParamObject> {
            override fun createFromParcel(source: Parcel): ParamObject = ParamObject(source)
            override fun newArray(size: Int): Array<ParamObject?> = arrayOfNulls(size)
        }
    }
}