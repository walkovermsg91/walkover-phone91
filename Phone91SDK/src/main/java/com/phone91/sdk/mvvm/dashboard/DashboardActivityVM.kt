package com.phone91.sdk.mvvm.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.net.UnknownHostException
import java.util.HashMap
import javax.inject.Inject


class DashboardActivityVM  constructor(var appDataManager: AppDataManager) : ViewModel() {

    var error = MutableLiveData<String>()
    var finishError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()

    var widgetInfo = MutableLiveData<String>()
    var channelListError = MutableLiveData<String>()
    var channelListData = MutableLiveData<ChannelListObject>()
    var channelListData1 = MutableLiveData<ChannelListObject>()
    var pubnubInfo = MutableLiveData<String>()
    var clientInfo = MutableLiveData<ClientObject>()
    var paramListInfo = MutableLiveData<ParamListObject>()
    var channelInfo = MutableLiveData<ChannelObject>()
//    var channelObjectData = MutableLiveData<ChannelObject>()
    var loginObjectData = MutableLiveData<LoginObject>()
    var widgetObjectData = MutableLiveData<WidgetDBObject>()
    val TAG: String = "DashboardActivityVM"
    private var compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var context: Application

    fun getClientDetail() {
        loading.value = true
        val loginDisposable = appDataManager.getClientDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {

                    if(value.code()==200) {

//            {"name":"Android","tagline":"SDK testing","teams":[{"id":93,"name":"Teating"}],"hide_launcher":false,"show_send_button":true,"show_close_button":true,"auto_focus":true}
                        if (value.body()?.toString()!!.startsWith("{")) {
                            Log.d("responce",value.body().toString())
                            widgetInfo.value = value.body()?.toString()
//                            error.value = value.body()?.get("msg").toString()
                        }
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                        else
                        error.value = e.message

                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }

//    "channel": <channel-name-if-already present>, // other parameters are ignored if channel passed
//    "name": <name>,
//    "number": <number>,
//    "mail": <mail>,
//    "unique_id": <unique_id>,
//    "team_id": <team_id>
    fun getChannelDetail(channel: String?, json: HashMap<String?, Any?>, unique_id: String?, team_id: String?) {
    Log.d("getChannelDetail","check")
        loading.value = true
        val loginDisposable = appDataManager.getChannelDetail(channel,json,unique_id,team_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"status":"Success","message":"channel created","body":{"id":104163,"channel":"ch-user-32.4f49ec6157494a93a56923984a4241e4",
//                        "uuid":"06f32e87-2bb5-4a9e-99fb-09db27c7b555","last_read":null,"team_id":null,"name":"Lime Lobster","number":null,"mail":null,"unique_id":null,
//                        "assigned_to":null,"is_closed":false}}
                    if ((value.code() == 201 || value.code() == 200) && value.body()?.get("success")?.asString.equals("true")) {

//            {"name":"Android","tagline":"SDK testing","teams":[{"id":93,"name":"Teating"}],"hide_launcher":false,"show_send_button":true,"show_close_button":true,"auto_focus":true}
                        if (value.body()?.toString()!!.startsWith("{")) {
                            Log.d("responce",value.body().toString())
                            var channelObject: ChannelObject;
                            if (value.body()?.has("body")!!) {
                                channelObject = Gson().fromJson(value.body()?.get("body")?.asJsonObject, ChannelObject::class.java)
                            } else {
                                channelObject = Gson().fromJson(value.body()?.get("data")?.asJsonObject, ChannelObject::class.java)
                            }
                           if(channelObject.team_id==null)
                               channelObject.team_id=team_id
                            channelInfo.value = channelObject
//                            error.value = value.body()?.get("msg").toString()
                        }
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }

    fun getChannelDetailHistory(channel :Channel?) {
        loading.value = true
        Log.d("getChannelDetailHistory","check")
        val loginDisposable = appDataManager.getChannelDetailHistory(channel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"status":"Success","message":"channel created","body":{"id":104163,"channel":"ch-user-32.4f49ec6157494a93a56923984a4241e4",
//                        "uuid":"06f32e87-2bb5-4a9e-99fb-09db27c7b555","last_read":null,"team_id":null,"name":"Lime Lobster","number":null,"mail":null,"unique_id":null,
//                        "assigned_to":null,"is_closed":false}}
                    if (value.code() == 200 && value.body()?.get("status")?.asString.equals("Success") ||
                        value.body()?.get("success")?.asString.equals("true")) {

//            {"name":"Android","tagline":"SDK testing","teams":[{"id":93,"name":"Teating"}],"hide_launcher":false,"show_send_button":true,"show_close_button":true,"auto_focus":true}
                        if (value.body()?.toString()!!.startsWith("{")) {
                            Log.d("responce",value.body().toString())
                            var channelObject: ChannelObject;
                            if (value.body()?.has("body")!!) {
                                channelObject = Gson().fromJson(value.body()?.get("body")?.asJsonObject, ChannelObject::class.java)
                            } else {
                                channelObject = Gson().fromJson(value.body()?.get("data")?.asJsonObject, ChannelObject::class.java)
                            }

                            if(channelObject.team_id==null)
                                channelObject.team_id=channel?.team_id
                            channelInfo.value = channelObject
//                            error.value = value.body()?.get("msg").toString()
                        }
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }

//    fun getChannelDetailFromDB( team_id : String?) {
//        var t=team_id
//        if(t==null)
//            t="default_team"
//        loading.value = true
//        val loginDisposable = appDataManager.getChannelByTeamID(t)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe( {
//                loading.value=false
//                var channelObject:ChannelObject=it
////                if(channelObject==null) {
////                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
//////                    channelObject.
////
////                }
//                channelObjectData.value=channelObject
//            },
//                { errors ->
//                    Log.e(TAG, "Unable to get username", errors)
//                    loading.value=false
//                     if(errors is EmptyResultSetException)
//                         channelObjectData.value=ChannelObject(-1,null,null,null,t,null,null,null,null/*,false,null*/)
//                    else
//                         error.value=errors.message
//                })
//        compositeDisposable.add(loginDisposable)
//    }
    fun insertOnDB(channelObject: ChannelObject) {
        loading.value=true
        val loginDisposable = appDataManager.insertChannel(channelObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                loading.value=false
//                var channelObject:ChannelObject=it
//                if(channelObject==null) {
//                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
////                    channelObject.
//
//                }
//                channelObjectData.value=channelObject
            },
                { error -> Log.e(TAG, "Unable to get username", error)
                    loading.value=false
                })
        compositeDisposable.add(loginDisposable)
    }

    fun insertLoginDetail(loginObject: LoginObject) {

//        loading.value=true
        val loginDisposable = appDataManager.insertLoginDetail(loginObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

//                loading.value=false
            },
                { error -> Log.e(TAG, "Unable to get username", error)
                    loading.value=false
                })
        compositeDisposable.add(loginDisposable)
    }

    fun deleteLoginDetail() {
        val loginDisposable = appDataManager.deleteLoginDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({


            },
                { error -> Log.e(TAG, "Unable to get username", error)

                })
        compositeDisposable.add(loginDisposable)
    }


    fun getPubnubDetail() {
        loading.value = true
        val loginDisposable = appDataManager.getPubnubDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                    if(value.code()==200 && value.body()?.toString()!!.startsWith("{")) {
                        Log.d("responce",value.body().toString())
                        pubnubInfo.value=value.body()?.toString()
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }
    fun getClientParam() {
        loading.value = true
        val loginDisposable = appDataManager.getClientParam()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                    if(value.code()==200 && value.body()?.toString()!!.startsWith("{")) {
                        Log.d("responce",value.body().toString())
                        var paramListObject = Gson().fromJson(value.body().toString(), ParamListObject::class.java)

                        Log.d("Hello3", paramListObject.toString())

                        paramListInfo.value=paramListObject
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }

    fun getChannelList(email: String?, uuid: String?) {
        loading.value = true
        val loginDisposable = appDataManager.getChannelList(email,uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                    if(value.code()==200 && value.body()?.toString()!!.startsWith("{")) {
                        Log.d("responce",value.body().toString())
//                        {"name":"Tipu","number":"9999999999","mail":"t2@sultan.com","unique_id":"9c62a8bc-facc-4deb-9143-d49dea7e4b54",
//                            "uuid":"4f7b8e9c-a06d-4953-af8b-326d73ea7a09","channels":[{"id":105857,"channel":"ch-user-32.8748e64199a847738dded3d1ee1eab22",
//                                "last_read":null,"team_id":95,"assigned_to":{"name":"Richa Shukla","username":"richa"},"is_closed":false}],"call_enabled":false}
                        var channelListObject = Gson().fromJson(value.body(), ChannelListObject::class.java)

//                        ChannelListObject(name=write_name, number=999999987, mail=aob@vb.com, unique_id=null,
//                            uuid=60e7333e-bd3f-495e-a319-a78a53e9f271, channel=null, call_enabled=false,
//                            channels=[Channel(id=106389, channel=ch-user-32.9859fc99784546fe8be36309c1541547,
//                                last_read=null, team_id=95, is_closed=false, assigned_to=null, showCallBtn=null, uuid=null),
//                                Channel(id=106392, channel=ch-user-32.1d577a99bd7b4b579fecdc09702b864a, last_read=null, team_id=93,
//                                    is_closed=false, assigned_to=null, showCallBtn=null, uuid=null)])
                        channelListData.value=channelListObject
                    }
                    else if(value.code()==404){

                        var responseBody = value.errorBody() as ResponseBody
                        val jObjError = JSONObject(responseBody.string())
                        Log.d("json",    jObjError.toString())
                        if (jObjError.toString().contains("client not found")) {
                            channelListError.value = "NOT FOUND"//value.message()
                        }
//                        channelListError.value=value.message()
                        }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }
    fun getChannelList1(email: String?, name: String?, phone: String?,uuid: String?) {
        loading.value = true
        val loginDisposable = appDataManager.getChannelList1(email,name,phone,uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                    if(value.code()==200 && value.body()?.toString()!!.startsWith("{")) {
                        Log.d("responce",value.body().toString())
//                        {"name":"Tipu","number":"9999999999","mail":"t2@sultan.com","unique_id":"9c62a8bc-facc-4deb-9143-d49dea7e4b54",
//                            "uuid":"4f7b8e9c-a06d-4953-af8b-326d73ea7a09","channels":[{"id":105857,"channel":"ch-user-32.8748e64199a847738dded3d1ee1eab22",
//                                "last_read":null,"team_id":95,"assigned_to":{"name":"Richa Shukla","username":"richa"},"is_closed":false}],"call_enabled":false}
                        var channelListObject = Gson().fromJson(value.body(), ChannelListObject::class.java)

                        if(channelListObject.name!=name && channelListObject.number==null)
                            setClient(appDataManager?.getChannel(),name!!,email!!,phone!!,uuid)
                        else
                            channelListData1.value=channelListObject

                        channelListData1.value=channelListObject
                    }
                    else if(value.code()==404){
                        channelListError.value=value.message()
                        }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)
    }

    public fun setClient(channel:String?,name: String, email:String, phone:String,uuid:String?){

        loading.value = true
        val loginDisposable = appDataManager.setClient(channel,name, email, phone, uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<JsonObject>>() {
                override fun onComplete() {
                    loading.value = false
                }

                override fun onNext(value: Response<JsonObject>) {
//                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                    if(value.code()==200 && value.body()?.toString()!!.startsWith("{")) {
                        Log.d("responce",value.body().toString())
//                        {"name":"Tipu","number":"9999999999","mail":"t2@sultan.com","unique_id":"9c62a8bc-facc-4deb-9143-d49dea7e4b54",
//                            "uuid":"4f7b8e9c-a06d-4953-af8b-326d73ea7a09","channels":[{"id":105857,"channel":"ch-user-32.8748e64199a847738dded3d1ee1eab22",
//                                "last_read":null,"team_id":95,"assigned_to":{"name":"Richa Shukla","username":"richa"},"is_closed":false}],"call_enabled":false}
                        Log.d("responce",value.body().toString())
                        var channelListObject = Gson().fromJson(value.body(), ClientObject::class.java)
                        clientInfo.value=channelListObject
                    }
                    else if(value.code()==404){
                        channelListError.value=value.message()
                    }
                    else {
                        error.value=value.message()
                    }

                }

                override fun onError(e: Throwable) {
                    if(e is UnknownHostException)
                        finishError.value=true
                    else
                        error.value = e.message
                    loading.value = false
                }
            })
        compositeDisposable.add(loginDisposable)

    }

    fun getLoginDetailFromDB() {
        loading.value = true
        val loginDisposable = appDataManager.getLoginDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                loading.value=false
                var loginObject:LoginObject=it
//                if(channelObject==null) {
//                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
////                    channelObject.
//
//                }
                loginObjectData.value=loginObject
            },
                { errors ->
                    Log.e(TAG, "Unable to get username", errors)
                    loading.value=false
                    if(errors is EmptyResultSetException)
                        loginObjectData.value= LoginObject(-1,null,null,null)
                    else
                        error.value=errors.message
                })
        compositeDisposable.add(loginDisposable)
    }

    fun getWidgetDetailFromDB(widgetToken: String?) {
        loading.value = true
        val loginDisposable = appDataManager.getWidgetDetail(widgetToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                loading.value=false
                var loginObject:WidgetDBObject=it
//                if(channelObject==null) {
//                    channelObject = ChannelObject(-1,null,null,null,team_id,null,null,null,null/*,false,null*/)
////                    channelObject.
//
//                }
                widgetObjectData.value=loginObject
            },
                { errors ->
                    Log.e(TAG, "Unable to get username", errors)
                    loading.value=false
                    if(errors is EmptyResultSetException)
                        widgetObjectData.value= WidgetDBObject("",null,null)
                    else
                        error.value=errors.message
                })
        compositeDisposable.add(loginDisposable)
    }

    fun insertWidgetDetail(widgetDBObject: WidgetDBObject) {
//        loading.value=true
        //      if(widgetDao.getWidgetDetail(widgetDBObject.widgetToken))
        val loginDisposable = appDataManager.insertWidgetDetail(widgetDBObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

//                loading.value=false
            },
                { error -> Log.e(TAG, "Unable to get username", error)
//                    loading.value=false
                })
        compositeDisposable.add(loginDisposable)
    }


}
