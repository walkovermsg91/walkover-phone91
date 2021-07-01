package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by richa.s 441 on 11/11/2019.
 */
//"id": 3,
//"genre_id": 1,
//"song_title": "test song",
//"audio_file": "1572696445.mp3",
//"status": 1,
//"created_at": "2019-10-31 11:06:49",
//"created_by": 2,
//"updated_at": "2019-11-02 17:44:16",
//"updated_by": 0


//"song_image":"https:\/\/kr..com\/amen\/public\/images\/song-images\/1574082562_19-11-18_365031_image.png",
//"description":"testtest testtest",
//"thumbimage_file":"https:\/\/kr..com\/amen\/public\/images\/song-images\/thumb-images\/1574082562_19-11-18_365031_image.png"

data class MusicObject(var id: String? = null,
                       var genre_id: Int? = null,
                       var song_title: String? = null,
                       var audio_file: String? = null,
                       var song_image: String? = null,
                       var description: String? = null,
                       var text_description: String? = null,
                       var thumbimage_file: String? = null,
                       var status: Int? = null,
                       var created_at: String? = null,
                       var created_by: Int? = null,
                       var updated_at: String? = null,
                       var updated_by: Int? = null
                         ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(genre_id)
        parcel.writeString(song_title)
        parcel.writeString(audio_file)
        parcel.writeString(song_image)
        parcel.writeString(description)
        parcel.writeString(text_description)
        parcel.writeString(thumbimage_file)
        parcel.writeValue(status)
        parcel.writeString(created_at)
        parcel.writeValue(created_by)
        parcel.writeString(updated_at)
        parcel.writeValue(updated_by)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicObject> {
        override fun createFromParcel(parcel: Parcel): MusicObject {
            return MusicObject(parcel)
        }

        override fun newArray(size: Int): Array<MusicObject?> {
            return arrayOfNulls(size)
        }
    }

}