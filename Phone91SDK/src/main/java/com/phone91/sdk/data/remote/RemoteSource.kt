package com.phone91.sdk.data.remote


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.phone91.sdk.model.Channel
import com.phone91.sdk.model.ChannelListObject
//import com.phone91.sdk.model.ProfileObject
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.Response

interface RemoteSource {

//    fun getAPIAccessToken(): Observable<Response<JsonObject>>
//    fun callSignup(/*rofileObject: ProfileObject,*/
//                   code: String,
//                   apnstoken: String): Observable<Response<JsonObject>>
//
//
//    fun getStateList(): Observable<Response<JsonObject>>
//    fun getLgaByStateId(id: String): Observable<Response<JsonObject>>
//
//    fun VerifyOTPAndResetPassword(
//        mobile: String,
//        otp: String,
//        password: String
//    ): Observable<Response<JsonObject>>
//
//
//
//
//    fun logout(): Observable<Response<JsonObject>>
//
//    fun sendOTP(mobile: String, otp: String): Observable<Response<JsonObject>>
//    fun getSubscription(): Observable<Response<JsonObject>>
//    fun getProjectDetails(id: String?): Observable<Response<JsonObject>>
////    fun saveSubscription(purchase: Purchase): Observable<Response<JsonObject>>
//    fun verifyAndSaveAppAccessMonthlySubscription(refence: String): Observable<Response<JsonObject>>
//    fun verifyAndSaveProjectMonthlySubscription(refence: String): Observable<Response<JsonObject>>
//    fun callForgotPassword(email: String): Observable<Response<JsonObject>>
//    fun callForgotPasswordPhone(phone: String): Observable<Response<JsonObject>>
//    fun callSignin(email: String, password: String, token: String?): Observable<Response<JsonObject>>
////    fun fbuserMobileVerification(
////        profileObject: ProfileObject,
////        name: JsonObject,
////        code: String,
////        apnstoken: String
////    ): Observable<Response<JsonObject>>
//    fun callFacebookAPI(userObj: JSONObject, token: String?): Observable<Response<JsonObject>>
//    fun changePassword(oldPassword: String, newPassword: String): Observable<Response<JsonObject>>
////    fun reSendOTP(profileObject: ProfileObject): Observable<Response<JsonObject>>
//    fun fbUserRequestMobileOTP(mobile: String): Observable<Response<JsonObject>>
//    fun getUserDetails(): Observable<Response<JsonObject>>
//    fun getCoinsHistory(): Observable<Response<JsonObject>>
//    fun  getMonthlyPlanDetails(): Observable<Response<JsonObject>>
//    fun getQuotes(): Observable<Response<JsonObject>>
//    fun getMusicList(page: Int): Observable<Response<JsonObject>>
//    fun getNotificationList(page: Int): Observable<Response<JsonObject>>
//    fun getProjectList(page: Int): Observable<Response<JsonObject>>
//    fun updateUserProfile(name: String, mobile: String?, user_img: String?): Observable<Response<JsonObject>>


    //// Ne wProject

    fun getClientDetail(): Observable<Response<JsonObject>>
    fun getChannelList(email: String?, uuid: String?): Observable<Response<JsonObject>>
    fun getChannelList1(email: String?, name: String?, phone: String?, uuid: String?): Observable<Response<JsonObject>>
    fun setClient(channel:String?,name: String, email:String, phone:String,uuid:String?): Observable<Response<JsonObject>>
    fun getChannelDetail(channel: String?, jsonParams: java.util.HashMap<String?, Any?>, unique_id : String?, team_id : String?): Observable<Response<JsonObject>>
    fun getChannelDetailHistory(channel : Channel?): Observable<Response<JsonObject>>
    fun getPubnubDetail(): Observable<Response<JsonObject>>
    fun getClientParam(): Observable<Response<JsonObject>>
    fun sendImage(imagePath :String): Observable<Response<JsonObject>>
    fun callAPI(key :String): Observable<Response<JsonObject>>

    fun sendTestMSG(msg: String): Observable<Response<JsonObject>>
    fun sendImageMessage(msg: String, msg_type: String, attachment: JsonElement): Observable<Response<JsonObject>>

}