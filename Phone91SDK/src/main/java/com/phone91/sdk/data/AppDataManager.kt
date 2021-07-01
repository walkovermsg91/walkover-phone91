package com.phone91.sdk.data

import android.app.Application
import android.content.Context
import com.google.gson.JsonObject
import com.phone91.sdk.data.database.DBManager
import com.phone91.sdk.data.preference.AppPreferenceManager
import com.phone91.sdk.data.remote.RemoteDataManager
import com.phone91.sdk.model.*
import com.phone91.sdk.module.MyApplicationModule
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import java.util.HashMap


class AppDataManager
private constructor(private var context: Context,
            private var remoteDataManager: RemoteDataManager,
            preferenceManager: AppPreferenceManager,
            dBManager: DBManager) : DataManager {




    companion object {
        private var  instance: AppDataManager?=null
        private lateinit var  myApplicationModule: MyApplicationModule
        private lateinit var appPreferenceManager: AppPreferenceManager
        private lateinit var remoteDataManager: RemoteDataManager
        private lateinit var appDBManager: DBManager
        fun  getInstance( app: Application): AppDataManager? {
            if(instance==null) {
                myApplicationModule=MyApplicationModule.getInstance(app)!!
                appPreferenceManager= myApplicationModule.getPreferenceSource()!!
                remoteDataManager= myApplicationModule.provideRemoteDataManager()!!
                appDBManager= myApplicationModule.provideAppDBManager()!!
                instance = AppDataManager(app.applicationContext, remoteDataManager,appPreferenceManager,appDBManager)
            }
            return instance
        }
    }

    override fun getWidgetToken(): String? {
        return appPreferenceManager.getWidgetToken()
    }

    override fun setWidgetToken(widgetToken: String) {
        appPreferenceManager.setWidgetToken(widgetToken)
    }


    override fun getWidgetDeviceId(): String? {
        return appPreferenceManager.getWidgetDeviceId()
    }

    override fun setMsg91DeviceId(msg91DeviceId: String?) {
        appPreferenceManager.setMsg91DeviceId(msg91DeviceId)
    }

    override fun getMsg91DeviceId(): String? {
        return appPreferenceManager.getMsg91DeviceId()
    }

    override fun setCallId(callId: String?) {
        appPreferenceManager.setCallId(callId)
    }

    override fun getCallId(): String? {
        return appPreferenceManager.getCallId()
    }

    override fun setWidgetDeviceId(widgetToken: String?) {
        appPreferenceManager.setWidgetDeviceId(widgetToken)
    }


    override fun getChannel(): String? {
        return appPreferenceManager.getChannel()
    }

    override fun setChannel(channel: String) {
        appPreferenceManager.setChannel(channel)
    }



    override fun getChatId(): String? {
        return appPreferenceManager.getChatId()
    }

    override fun setChatId(chatId: String) {
        appPreferenceManager.setChatId(chatId)
    }


    override fun getUUID(): String? {
        return appPreferenceManager.getUUID()
    }

    override fun logoutFromPreffrence(){
        return appPreferenceManager.remove()
    }
    override fun setUUID(UUID: String?) {
        appPreferenceManager.setUUID(UUID)
    }


    override fun getUniqId(): String? {
        return appPreferenceManager.getUniqId()
    }

    override fun setUniqId(UniqId: String) {
        appPreferenceManager.setUniqId(UniqId)
    }



    override fun getPubkey(): String? {
        return appPreferenceManager.getPubkey()
    }

    override fun setPubkey(pubkey: String) {
        appPreferenceManager.setPubkey(pubkey)
    }

    override fun getSubkey(): String? {
        return appPreferenceManager.getSubkey()
    }

    override fun setSubkey(subkey: String) {
        appPreferenceManager.setSubkey(subkey)
    }

    override fun getAuth(): String? {
        return appPreferenceManager.getAuth()
    }

    override fun setAuth(auth: String) {
        appPreferenceManager.setAuth(auth)
    }

    override fun getChannelByTeamID(teamid: String): Single<ChannelObject> {
        return appDBManager.getChannelByTeamID(teamid)
    }

    override fun getLoginDetail(): Single<LoginObject> {
        return appDBManager.getLoginDetail()
    }



    override fun getNameByChannel(channel: String): Single<ChannelObject> {
        return appDBManager.getNameByChannel(channel)
    }

override fun isFavouriteShow(teamid: String): Single<ChannelObject> {
        return appDBManager.getChannelByTeamID(teamid)
    }

    override fun insertChannel(channelObject: ChannelObject) : Completable {
       return appDBManager.insertChannel(channelObject)
    }

    override fun insertLoginDetail(loginObject: LoginObject): Completable {
        return appDBManager.insertLoginDetail(loginObject)
    }

    override fun deleteLoginDetail(): Completable  {
        return appDBManager.deleteLoginDetail()
    }

    override fun getWidgetDetail(widgetToken: String?): Single<WidgetDBObject> {
       return appDBManager.getWidgetDetail(widgetToken)
    }

    override fun insertWidgetDetail(widgetDBObject: WidgetDBObject): Completable {
       return appDBManager.insertWidgetDetail(widgetDBObject)
    }


    override fun getClientDetail(): Observable<Response<JsonObject>> =
        remoteDataManager.getClientDetail()

    override fun getChannelDetail(channel: String?, json: HashMap<String?, Any?>, unique_id : String?, team_id : String?): Observable<Response<JsonObject>> =
        remoteDataManager.getChannelDetail(channel,json,unique_id,team_id)

    override fun getChannelDetailHistory(channel : Channel?): Observable<Response<JsonObject>> =
        remoteDataManager.getChannelDetailHistory(channel)

    override fun getPubnubDetail(): Observable<Response<JsonObject>> =
        remoteDataManager.getPubnubDetail()

    override fun getClientParam(): Observable<Response<JsonObject>> =
        remoteDataManager.getClientParam()

    override fun getChannelList(email: String?, uuid: String?): Observable<Response<JsonObject>> =
        remoteDataManager.getChannelList(email,uuid)
    override fun getChannelList1(email: String?, name: String?, phone: String?, uuid: String?): Observable<Response<JsonObject>> =
        remoteDataManager.getChannelList1(email,name,phone,uuid)

    override fun setClient(channel:String?,name: String, email:String, phone:String,uuid:String?): Observable<Response<JsonObject>> =
        remoteDataManager.setClient(channel,name, email, phone, uuid)

    override fun sendImage(imagePath :String): Observable<Response<JsonObject>> =
        remoteDataManager.sendImage(imagePath)


    override fun callAPI(key :String): Observable<Response<JsonObject>> =
        remoteDataManager.callAPI(key)



//    fun addPost(
//        caption: String,location: String, image: String,publish_date: String,file_type:String,device_type:String,tagged_ids:String
//    ): Observable<Response<JsonObject>> =
//            remoteDataManager.addPost(caption, location, image,publish_date,file_type,device_type,tagged_ids)







}



