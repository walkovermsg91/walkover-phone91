package com.phone91.sdk.data.remote


//import com.phone91.sdk.model.ProfileObject

import android.util.Log
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.phone91.sdk.data.preference.AppPreferenceManager
import com.phone91.sdk.model.Channel
import com.phone91.sdk.model.ChannelListObject
import io.reactivex.Observable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result.response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Part
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RemoteDataManager  constructor(private val appPreferenceManager: AppPreferenceManager) :
    RemoteSource {



    private var iApiService: IAuthApiService? = null


    private lateinit var webServiceInstance: Retrofit


    private var URL_PRODUCTION="https://api.phone91.com"
    private var URL_STAGE="https://testapi.phone91.com"
    private var URL_P=URL_PRODUCTION



    private fun initializeWebServer() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        httpClient.writeTimeout(30, TimeUnit.SECONDS)


        if (appPreferenceManager.getWidgetToken() != null) {
            Log.d("header",appPreferenceManager.getWidgetToken())
            httpClient.addInterceptor(Interceptor() { chain: Interceptor.Chain ->
                var original = chain.request()
                var request = original.newBuilder()
//                Authorization
//                 .addHeader("Authorization", appPreferenceManager.getWidgetToken())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "keep-alive")
                    //.addHeader("Authorization", appPreferenceManager.getWidgetToken())
                    .method(original.method, original.body)
                    .build();
                chain.proceed(request)
            })

        }

        webServiceInstance = Retrofit.Builder()


            .baseUrl(URL_P )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())

            .build()
        iApiService = webServiceInstance.create(IAuthApiService::class.java)

    }

    override fun getClientDetail(): Observable<Response<JsonObject>> {


        if (iApiService == null) {
            initializeWebServer()
        }
        return iApiService!!.getClientDetail(appPreferenceManager.getWidgetToken()!!)
    }



    override fun getChannelDetail(channel: String?, jsonParams: java.util.HashMap<String?, Any?>, unique_id : String?, team_id : String?): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }


//Sss
        var channel1: RequestBody? = null
        var name1: RequestBody? = null
        var number1: RequestBody? = null
        var mail1: RequestBody? = null
        var team_id1: RequestBody? = null


        if (channel != null)
            jsonParams.put("channel",channel)

        if (unique_id != null)
            jsonParams.put("uuid",unique_id)
        if (team_id != null)
            jsonParams.put("team_id",Integer.parseInt(team_id))
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )


        var token=appPreferenceManager.getWidgetToken()!!

        return iApiService!!.getChannelDetail(body,/*channel1,name1,number1,mail1,unique_id1,team_id1,*/token)
    }



    override fun  getChannelDetailHistory(channel : Channel?): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }


        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            Gson().toJson(channel)
        )


        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+channel?.uuid
        return iApiService!!.getChannelDetail(body,/*channel1,name1,number1,mail1,unique_id1,team_id1,*/token)
    }

    override fun getPubnubDetail(): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }
        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+appPreferenceManager.getUUID()
        return iApiService!!.getPubnubDetail(token)
    }

    override fun getClientParam(): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }
        return iApiService!!.getClientParam(appPreferenceManager.mWidgetToken!!)
    }

    override fun getChannelList(email: String?, uuid: String?): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }


//Sss

        var mail1: RequestBody? = null
        var unique_id1: RequestBody? = null

        if (email != null)
            mail1 = RequestBody.create("text/plain".toMediaTypeOrNull(), email)


        if (uuid != null)
            unique_id1 = RequestBody.create("text/plain".toMediaTypeOrNull(), uuid)
        val jsonParams: HashMap<String?, Any?> = HashMap()
        jsonParams.put("mail",email)
//        jsonParams.put("unique_id",uuid)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )

        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+appPreferenceManager.getUUID()

        return iApiService!!.getChannelList(body,token)
    }
    override fun getChannelList1(email: String?, name: String?, phone: String?, uuid: String?): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }


//Sss

        var mail1: RequestBody? = null
        var unique_id1: RequestBody? = null

        if (email != null)
            mail1 = RequestBody.create("text/plain".toMediaTypeOrNull(), email)


        if (uuid != null)
            unique_id1 = RequestBody.create("text/plain".toMediaTypeOrNull(), uuid)
        val jsonParams: HashMap<String?, Any?> = HashMap()
        jsonParams.put("mail",email)
        jsonParams.put("name",name)
        jsonParams.put("number",phone)
//        jsonParams.put("unique_id",uuid)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )
//        response = hereIsYourInterfaceName().apicall(body).execute()




//        team_id1 = RequestBody.create(MediaType.parse("text/plain"), team_id)
        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+appPreferenceManager.getUUID()

        return iApiService!!.getChannelList(body,token)
    }
    override  fun setClient(channel:String?,name: String, email:String, phone:String,uuid:String?): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }
        val jsonParams: HashMap<String?, Any?> = HashMap()
        if (channel != null)
            jsonParams.put("channel",channel)
        if (name != null)
            jsonParams.put("param_1",name)
        if (email != null)
            jsonParams.put("param_3",email)
        if (phone != null)
            jsonParams.put("param_2",phone)
        if (uuid != null)
            jsonParams.put("uuid",uuid)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )
        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+appPreferenceManager.getUUID()
        return iApiService!!.setClient(body,token)
    }
    override fun sendImage(imagePath :String): Observable<Response<JsonObject>> {
        if (iApiService == null) {
            initializeWebServer()
        }
        var image: MultipartBody.Part? = null
        if (imagePath != null) {
            val file = File(imagePath)
            //val fbody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val mimeType:String = getMimeType(imagePath)!!
            val fbody = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
            image = MultipartBody.Part.createFormData("attachment", file.name, fbody)
        }
        var token = appPreferenceManager.getWidgetToken()!!
        if (appPreferenceManager.getUUID() != null)
            token = token + ":" + appPreferenceManager.getUUID()
        return iApiService!!.sendImage("chat", token, image)
    }


    override fun callAPI(key :String): Observable<Response<JsonObject>> {
        if (iApiService== null) {
            initializeWebServer()
        }

        var token=appPreferenceManager.getWidgetToken()!!
        if(appPreferenceManager.getUUID()!=null)
            token=token+":"+appPreferenceManager.getUUID()
        return iApiService!!.callAPI(token,key)
    }
    override fun sendTestMSG(msg: String): Observable<Response<JsonObject>> {
        if (iApiService == null) {
            initializeWebServer()
        }
        val jsonParams: HashMap<String?, Any?> = HashMap()
        jsonParams.put("type", "widget")
        jsonParams.put("message_type", "text")
        jsonParams.put("chat_id", appPreferenceManager.getChatId())

        val jsonParams1: HashMap<String?, Any?> = HashMap()
        jsonParams1.put("text", msg)
        val emptyStringArray = arrayOf<String>()
        jsonParams1.put("attachment", emptyStringArray)
        jsonParams.put("content", jsonParams1)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )
        var token = appPreferenceManager.getWidgetToken()!!
        if (appPreferenceManager.getUUID() != null)
            token = token + ":" + appPreferenceManager.getUUID()
        return iApiService!!.sendTestMSG(body, token)
    }

    override fun sendImageMessage(
        msg: String,
        msg_type: String,
        attachment: JsonElement
    ): Observable<Response<JsonObject>> {
        if (iApiService == null) {
            initializeWebServer()
        }
        val jsonParams1 = JSONObject().
        put("text", msg).
        put("attachment",
            JSONArray().put(JSONObject(attachment.asJsonObject.toString().replace("\\",""))))
        val jsonParams=JSONObject()
            .put("type", "widget")
            .put("message_type", msg_type)
            .put("chat_id", appPreferenceManager.getChatId())
            .put("content", jsonParams1)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonParams.toString()
        )

        var token = appPreferenceManager.getWidgetToken()!!
        if (appPreferenceManager.getUUID() != null)
            token = token + ":" + appPreferenceManager.getUUID()
        return iApiService!!.sendTestMSG(body, token)

    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension: String = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

}

