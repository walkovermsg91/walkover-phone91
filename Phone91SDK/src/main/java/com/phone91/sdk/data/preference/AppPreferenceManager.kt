package com.phone91.sdk.data.preference

import android.content.Context
//import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class AppPreferenceManager
//@Inject
constructor(private val context: Context, prefFileName: String) : Preferences(), PreferenceSource {


    override fun getWidgetToken(): String? {
        return this.mWidgetToken
    }

    override fun setWidgetDeviceId(widgetDeviceId: String?) {
        this.mWidgetDeviceId = widgetDeviceId
    }

    override fun getWidgetDeviceId(): String? {
        return this.mWidgetDeviceId
    }

    override fun setMsg91DeviceId(msg91DeviceId: String?) {
        this.mMsg91DeviceId = msg91DeviceId
    }

    override fun getMsg91DeviceId(): String? {
        return this.mMsg91DeviceId
    }

    override fun setCallId(callId: String?) {
        this.mCallId = callId
    }

    override fun getCallId(): String? {
        return this.mCallId
    }


    override fun setWidgetToken(widgetToken: String) {
        this.mWidgetToken = widgetToken
    }

    override fun getChannel(): String? {
        return this.mChannel
    }

    override fun setChannel(channel: String) {
        this.mChannel = channel
    }





    override fun getUUID(): String? {
        return this.mUUID
    }
    override fun logoutFromPreffrence(){
        remove()
    }

    override fun getChatId(): String? {
       return mChatId
    }

    override fun setChatId(chatId: String) {
        this.mChatId=chatId
    }

    override fun setUUID(UUID: String?) {
        this.mUUID = UUID
    }

    override fun getPubkey(): String? {
        return this.mPubkey
    }

    override fun setPubkey(pubkey: String) {
        this.mPubkey = pubkey
    }

    override fun getSubkey(): String? {
        return this.mSubkey
    }

    override fun setSubkey(subkey: String) {
        this.mSubkey = subkey
    }


    override fun getAuth(): String? {
        return this.mAuth
    }

    override fun setAuth(auth: String) {
        this.mAuth = auth
    }
     override fun getUniqId(): String? {
        return this.mUniqId
    }

    override fun setUniqId(UniqId: String) {
        this.mUniqId = UniqId
    }




    init {
        Preferences.init(context, prefFileName)
    }

    var mWidgetToken by stringPref("widgetToken")
    var mChannel by stringPref("channel")
    var mUUID by stringPref("UUID")
    var mPubkey by stringPref("pubkey")
    var mSubkey by stringPref("subkey")
    var mAuth by stringPref("auth")
    var mUniqId by stringPref("uniqId")
    var mWidgetDeviceId by stringPref("WidgetDeviceId")
    var mMsg91DeviceId by stringPref("Msg91DeviceId")
    var mCallId by stringPref("CallId")
    var mChatId by stringPref("ChatId")


}