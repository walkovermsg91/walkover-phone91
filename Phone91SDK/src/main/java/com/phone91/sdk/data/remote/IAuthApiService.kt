package  com.phone91.sdk.data.remote

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import javax.annotation.PostConstruct


interface IAuthApiService {


    //@GET("/v2/widget-info/")
    @GET("/widget-info/")
    fun getClientDetail(@Header("Authorization") auth: String): Observable<Response<JsonObject>>

    //@POST("/v2/pubnub-channels/")
    @POST("/pubnub-channels/")
    fun getChannelDetail(
        @Body channel: RequestBody?,
        @Header("Authorization") authorization: String
    ): Observable<Response<JsonObject>>


    //@POST("/v2/pubnub-channels/list/")
    @POST("/pubnub-channels/list/")
    fun getChannelList(
        @Body channel: RequestBody?,
        @Header("Authorization") authorization: String
    ): Observable<Response<JsonObject>>

    //@PUT("/v2/client/")
    @PUT("/client/")
    fun setClient(
        @Body channel: RequestBody?,
        @Header("Authorization") authorization: String
    ): Observable<Response<JsonObject>>


    //@GET("/v2/pubnub-keys/")
    @GET("/pubnub-keys/")
    fun getPubnubDetail(@Header("Authorization") authorization: String): Observable<Response<JsonObject>>

    @GET("/call-session-creds/{chat-id}")
    fun callAPI(
        @Header("Authorization") authorization: String,
        @Path("chat-id") chat_id: String
    ): Observable<Response<JsonObject>>

    //@GET("/v2/client-param/")
    @GET("/client-param/")
    fun getClientParam(@Header("Authorization") authorization: String): Observable<Response<JsonObject>>

    //@Multipart
    //@POST("/v2/chat-attachment/")
    @Multipart
    @POST("/chat-attachment/")
    fun sendImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part?
    ): Observable<Response<JsonObject>>


}
