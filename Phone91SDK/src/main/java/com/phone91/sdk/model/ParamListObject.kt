package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

/**
 * Created by richa WALK311 on 09/04/2020.
 */

//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}

//{"id":1587385161501,"content":"cccc","sender":"richa","type":"chat","attachment_url":"","mime_type":""}
data class ParamListObject(
    var default_params: ArrayList<ParamObject>? = null,
    var custom_params: ArrayList<ParamObject>? = null,
    var standard_params: ArrayList<ParamObject>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createTypedArrayList(ParamObject.CREATOR),
        source.createTypedArrayList(ParamObject.CREATOR),
        source.createTypedArrayList(ParamObject.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(default_params)
        writeTypedList(custom_params)
        writeTypedList(standard_params)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ParamListObject> =
            object : Parcelable.Creator<ParamListObject> {
                override fun createFromParcel(source: Parcel): ParamListObject =
                    ParamListObject(source)

                override fun newArray(size: Int): Array<ParamListObject?> = arrayOfNulls(size)
            }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}