package com.phone91.sdk.data.preference

//import com.google.android.gms.maps.model.LatLng


interface PreferenceSource {

    fun setWidgetToken(widgetToken: String)
    fun getWidgetToken(): String?

    fun setWidgetDeviceId(widgetDeviceId: String?)
    fun getWidgetDeviceId(): String?

    fun setMsg91DeviceId(msg91DeviceId: String?)
    fun getMsg91DeviceId(): String?


    fun setCallId(callId: String?)
    fun getCallId(): String?




    fun getChannel(): String?

    fun setChannel(channel: String)
    fun getUUID(): String?
    fun setUUID(UUID: String?)
     fun getPubkey(): String?
     fun setPubkey(pubkey: String)
     fun getSubkey(): String?
     fun setSubkey(subkey: String)
     fun getAuth(): String?
     fun setAuth(auth: String)

    fun getUniqId(): String?
     fun setUniqId(UniqId: String)
     fun logoutFromPreffrence()


     fun getChatId(): String?


     fun setChatId(chatId: String)

}