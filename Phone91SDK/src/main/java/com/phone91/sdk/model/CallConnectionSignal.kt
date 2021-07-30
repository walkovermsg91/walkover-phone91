package com.phone91.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by richa WALK311 on 09/04/2020.
 */
//{
//    CallId: string;
//    type: 'CALL';
//    CallSignal:
//    | 'PERMIT-CALL-AUDIO'
//    | 'PERMIT-CALL-VIDEO'
//    | 'ACCEPT-PERMIT-AUDIO'
//    | 'ACCEPT-PERMIT-VIDEO'

//    WidgetDeviceId?: string;
//    Msg91DeviceId?: string;
//    sender?: string;
//    msg?: string;
//    widgetToken?: string;
//    CallType?: string;
//}
//type: string; \\ join-room
//roomUrl: string; \\ room url for join room
//WidgetDeviceId: string; \\check for active widgetDeviceId
//CallId: string; \\check for active widgetDeviceId
//Msg91DeviceId: string; \\check for active widgetDeviceId
//CallType: string; \\ AUDIO || VIDEO


//var content: String?  = null,
//var id: String? = null,
//var attachment_url: String?=null,
//var mime_type: String?=null,
//var notify: String?=null,
//var pn_gcm:PnGcmObject?=null,
//var time:Long=-1


@JsonIgnoreProperties(ignoreUnknown = true)
data class CallConnectionSignal(
    var CallId: String? = null,
    var type: String? = null,
    var CallSignal: String? = null,
    var WidgetDeviceId: String? = null,
    var Msg91DeviceId: String? = null,
    var sender_id: String? = null,
    var msg: String? = null,
    var widgetToken: String? = null,
    var CallType: String? = null,
    var roomUrl: String? = null,
    var content: String? = null,
    var id: String? = null,
    var attachment_url: String? = null,
    var mime_type: String? = null,
    var notify: String? = null,
    var pn_gcm: PnGcmObject? = null,
    var time: Long=-1
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readParcelable<PnGcmObject>(PnGcmObject::class.java.classLoader),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(CallId)
        writeString(type)
        writeString(CallSignal)
        writeString(WidgetDeviceId)
        writeString(Msg91DeviceId)
        writeString(sender_id)
        writeString(msg)
        writeString(widgetToken)
        writeString(CallType)
        writeString(roomUrl)
        writeString(content)
        writeString(id)
        writeString(attachment_url)
        writeString(mime_type)
        writeString(notify)
        writeParcelable(pn_gcm, 0)
        writeLong(time)
    }

    companion object {
        public val PREFIX_widgetDeviceId = "UUIDV4"

        public val ROOM_SIGNAL = "ROOM_SIGNAL"

        public val AUDIO = "AUDIO"

        public val VIDEO = "VIDEO"

        public val PERMIT_CALL_VIDEO = "PERMIT-CALL-VIDEO"

        public val PERMIT_CALL_AUDIO = "PERMIT-CALL-AUDIO"

        public val ACCEPT_PERMIT_AUDIO = "ACCEPT-PERMIT-AUDIO"

        public val ACCEPT_PERMIT_VIDEO = "ACCEPT-PERMIT-VIDEO"

        public val DECLINE_PERMIT_AUDIO = "DECLINE-PERMIT-AUDIO"

        public val DECLINE_PERMIT_VIDEO = "DECLINE-PERMIT-VIDEO"

        public val INIT_CALL_AUDIO = "INIT-CALL-AUDIO"

        public val INIT_CALL_VIDEO = "INIT-CALL-VIDEO"


        public val END_CALL_AUDIO = "END-CALL-AUDIO"

        public val END_CALL_VIDEO = "END-CALL-VIDEO"

        public val CALL_BUSY = "CALL-BUSY"

        public val CALL_ASSIGNED = "CALL-ASSIGNED"

        public val CALL_STARTED = "CALL-STARTED"

        public val VIDEO_JOIN_CALL_TIMEOUT = "VIDEO-JOIN-CALL-TIMEOUT"

        public val AUDIO_JOIN_CALL_TIMEOUT = "AUDIO-JOIN-CALL-TIMEOUT"

        public val VIDEO_JOIN_CALL_ACCEPT = "VIDEO-JOIN-CALL-ACCEPT"

        public val AUDIO_JOIN_CALL_ACCEPT = "AUDIO-JOIN-CALL-ACCEPT"

        public val VIDEO_JOIN_CALL_REJECT = "VIDEO-JOIN-CALL-REJECT"

        public val AUDIO_JOIN_CALL_REJECT = "AUDIO-JOIN-CALL-REJECT"

        public val MISS_CALL_AUDIO = "MISS-CALL-AUDIO"

        public val MISS_CALL_VIDEO = "MISS-CALL-VIDEO"

        public val CALL_END = "CALL-END"

        @JvmField
        val CREATOR: Parcelable.Creator<CallConnectionSignal> =
            object : Parcelable.Creator<CallConnectionSignal> {
                override fun createFromParcel(source: Parcel): CallConnectionSignal =
                    CallConnectionSignal(source)

                override fun newArray(size: Int): Array<CallConnectionSignal?> = arrayOfNulls(size)
            }
    }
}