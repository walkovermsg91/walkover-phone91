package com.phone91.sdk.mvvm.dashboard.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.phone91.sdk.data.AppDataManager
//import com.phone91.sdk.model.QouteObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.google.gson.reflect.TypeToken
import com.phone91.sdk.model.ChannelObject
import com.phone91.sdk.model.ClientObject
import com.phone91.sdk.model.RoomObject


class HomeViewModel : ViewModel() {

    var error = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var urlData = MutableLiveData<JsonObject>()
    var sendData = MutableLiveData<String>()
    var roomData = MutableLiveData<RoomObject>()

    private var compositeDisposable = CompositeDisposable()
    private var appDataManager:AppDataManager?=null


    public fun setAppDataManager(app:Application){
        appDataManager= AppDataManager.getInstance(app)
    }




    fun shareFile(image: String) {
        loading.value = true
        val loginDisposable = appDataManager?.sendImage(image)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe( {
//                {"status":"Success","message":"attachment uploaded successfully",
//                    "body":{"attachment_url":"https://test.phone91.com/static/32/1587641131-IMG_20200423_141630.jpg"}}

                loading.value=false

               /*if( it.body()?.has("status")!! && it.body()?.get("status")?.asString.equals("Success")){
                   urlData.value= it.body()?.get("body")?.asJsonObject?.get("attachment_url")?.asString
               }*/
                if (it.body()?.has("success")!! && it.body()?.get("success")?.asString.equals("true")) {
                    urlData.value = it.body()?.get("data")?.asJsonArray?.get(0)?.asJsonObject
                }
//                var channelObject: ChannelObject =it
//                if(channelObject==null) {
//                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
////                    channelObject.
//
//                }
//                channelObjectData.value=channelObject
            },
                { errors ->
                    //                    Log.e(TAG, "Unable to get username", errors)
                    loading.value=false

                    error.value=errors.message
                })
        compositeDisposable.add(loginDisposable!!)
    }

    fun callAPI(key: String) {
        loading.value = true
        val loginDisposable = appDataManager?.callAPI(key)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

            ?.subscribe( {
//                {"status":"Success","message":"attachment uploaded successfully",
//                    "body":{"attachment_url":"https://test.phone91.com/static/32/1587641131-IMG_20200423_141630.jpg"}}
//                Response{protocol=http/1.1, code=401, message=UNAUTHORIZED, url=https://testapi.phone91.com/call-session-creds/107604}
                loading.value=false
//                {"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjb250ZXh0Ijp7InVzZXIiOnsibmFtZSI6IlBsdW0gSG9yc2UiLCJlbWFpbCI6ImJoaUBiLmNvbSJ9fSwiYXVkIjoiaml0c2kiLCJpc3MiOiJo
//                    ZWxsbyIsInN1YiI6Im1hcmxpbi5waG9uZTkxLmNvbSIsInJvb20iOiJjYjBkZGQxM2Y0NTI0OWY0YTk0NDRmYmQyNGYxM2FjNiIsImV4cCI6MTYwMjEwMTk1MSwibW9kZXJhdG9yIjpm
//                    YWxzZX0.bYk4gkdOEj67LkViCGbuZFApk3feeOeGyZwXlyWCN1M","room":"cb0ddd13f45249f4a9444fbd24f13ac6"}
                if( it.code()==200){
                    var roomObject = Gson().fromJson(it.body(), RoomObject::class.java)
                    roomData.value=roomObject
                }
//                var channelObject: ChannelObject =it
//                if(channelObject==null) {
//                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
////                    channelObject.
//
//                }
//                channelObjectData.value=channelObject
            },
                { errors ->
                    //                    Log.e(TAG, "Unable to get username", errors)
                    loading.value=false

                    error.value=errors.message
                })
        compositeDisposable.add(loginDisposable!!)
    }
    fun sendTextMSG(msg: String) {
        loading.value = true
        val loginDisposable = appDataManager?.sendTestMSG(msg)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                loading.value = false
                if (it.body()?.has("success")!!&& it.body()?.get("success")?.asString.equals("true")){
                    sendData.value = it.body()?.asJsonObject.toString()
                }
            }, { errors ->
                loading.value = false
                error.value = errors.message
            })
        compositeDisposable.add(loginDisposable!!)
    }
    fun sendImageMessage(msg: String,msg_type: String, attachment: JsonElement) {
        loading.value = true
        val loginDisposable = appDataManager?.sendImageMessage(msg,msg_type,attachment)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                loading.value = false
                if (it.body()?.has("success")!!&& it.body()?.get("success")?.asString.equals("true")){
                    sendData.value = it.body()?.asJsonObject.toString()
                }
            }, { errors ->
                loading.value = false
                error.value = errors.message
            })
        compositeDisposable.add(loginDisposable!!)

    }
}
