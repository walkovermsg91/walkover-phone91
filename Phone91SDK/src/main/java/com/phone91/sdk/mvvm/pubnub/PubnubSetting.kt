package com.phone91.sdk.mvvm.pubnub

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.JsonNode
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.model.CallConnectionSignal
import com.phone91.sdk.model.CallStatusSignal
import com.phone91.sdk.model.ChatObject
import com.phone91.sdk.mvvm.dashboard.home.ChatAdapter
import com.phone91.sdk.mvvm.dashboard.home.ChatCallback
import com.phone91.sdk.utils.JsonUtil
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.history.PNHistoryResult
import kotlinx.android.synthetic.main.fragment_home_new.*
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class  PubnubSetting{

    init {
        println("Singleton class invoked.")
    }
    private var PUBSUB_CHANNEL: ArrayList<String>?=null
    lateinit var config: PNConfiguration

    private var mPubnub_DataStream: PubNub? = null
    lateinit var chatCallback: ChatCallback
    private var pnSendMsgCallback: PNSendMsgCallback?=null
    private var appDataManager: AppDataManager?=null
//    fun printVarName(){
//        println(variableName)
//    }

     fun initPubNub(appDataManager: AppDataManager) {
         this.appDataManager=appDataManager
//         if(config==null) {
             config = PNConfiguration()
             config.publishKey =/* "pub-c-81312503-4271-4283-9bf9-3ecd6d9254a2"*/
                 appDataManager.getPubkey()
             config.subscribeKey =/*"sub-c-2b6260c4-7640-11ea-808e-bad180999bc3"*/
                 appDataManager.getSubkey() //Replace with your subscribe key
             config.authKey = appDataManager.getAuth()
             config.uuid = appDataManager.getUUID()
             config.isSecure = true
         config.setReconnectionPolicy(PNReconnectionPolicy.LINEAR)

             this.mPubnub_DataStream = PubNub(config)
//             mPubnub_DataStream.removeListener()

//         }
    }

    fun setChannel(channel:String){
        PUBSUB_CHANNEL=ArrayList()
        PUBSUB_CHANNEL?.add(channel)
    }


     fun initChannels(chatAdapter:ChatAdapter, rvPosts:RecyclerView, pnSendMsgCallback: PNSendMsgCallback) {
         this.pnSendMsgCallback=pnSendMsgCallback
         if(config!=null){
             chatCallback = ChatCallback(chatAdapter, rvPosts)
             this.mPubnub_DataStream?.addListener(chatCallback)


             this.mPubnub_DataStream?.history()
                 ?.channel(PUBSUB_CHANNEL?.get(0))
//            ?.reverse(false)
                 ?.count(100)
                 ?.includeTimetoken(true)
                  ?.async { result, status ->

                      //                     override fun onResponse(result: PNHistoryResult, status: PNStatus) {
                      //                         if (!status.isError && !result.messages.isEmpty()) {
                      //                             for (message in result.messages) {
                      //                                 val jsonMsg: JsonNode = message.entry
                      //                                 val chatObject: CallConnectionSignal =
                      //                                     JsonUtil.convert(jsonMsg, CallConnectionSignal::class.java)
                      //                                 chatObject.time=message.timetoken
                      //                                 chatAdapter.add(chatObject)
                      //
                      //                             }
                      //
                      //                         }
                      //                         if(chatAdapter.itemCount>0)
                      //                           rvPosts.scrollToPosition(chatAdapter.itemCount - 1)
                      //                     }
                      if (!status.isError && !result?.messages?.isEmpty()!!) {
                          for (message in result.messages) {
                              val jsonMsg = getFinalString(message.entry)
                              val callConnectionSignal =
                                  Gson().fromJson(jsonMsg, CallConnectionSignal::class.java)
                              callConnectionSignal?.time = message.timetoken

                              if (callConnectionSignal?.CallType != null||callConnectionSignal?.type!=null && callConnectionSignal.CallSignal != null) {

                                  if (callConnectionSignal.CallSignal == CallConnectionSignal.VIDEO_JOIN_CALL_TIMEOUT) {
                                      if (callConnectionSignal.CallId?.startsWith("w-")!!)
                                          callConnectionSignal.msg = "Your video call did not connect"
                                      else
                                          callConnectionSignal.msg =
                                              "You missed the video call from agent"

                                  } else if (callConnectionSignal.CallSignal == CallConnectionSignal.AUDIO_JOIN_CALL_TIMEOUT) {
                                      if (callConnectionSignal.CallId?.startsWith("w-")!!)
                                          callConnectionSignal.msg = "Your audio call did not connect"
                                      else
                                          callConnectionSignal.msg =
                                              "You missed the audio call from agent"
                                  }



//
//                                  if (callConnectionSignal.CallId != null && callConnectionSignal.CallId?.startsWith(
//                                          "w-"
//                                      )!!) {
//                                      callConnectionSignal.msg =
//                                          if (callConnectionSignal?.CallType == CallConnectionSignal.AUDIO) "Your audio call did not connect" else "Your video call did not connect"
//                                  } else {
//                                      callConnectionSignal.msg =
//                                          if (callConnectionSignal?.CallType == CallConnectionSignal.AUDIO) "You missed the audio call from agent" else "You missed the video call from agent"
//                                  }
                              }


                              chatAdapter.add(callConnectionSignal)

                          }

                      }
                      if(chatAdapter.itemCount>0)
                          rvPosts.scrollToPosition(chatAdapter.itemCount - 1)
                  }

             this.mPubnub_DataStream?.subscribe()
                 ?.channels(PUBSUB_CHANNEL)
                 ?.withPresence()?.execute()

         } else
             Log.d("ConfigError","Call initPubNub() first")

    }
    private fun getFinalString(jsonMsg: JsonElement?): String? {
        val jsonObject: JSONObject = JSONObject(jsonMsg?.asJsonObject.toString())
        val jsonObject1: JSONObject = JSONObject(jsonMsg?.asJsonObject.toString())
        //Log.e("jsonMsg5555666", jsonObject.toString())
        if (jsonObject.opt("type").equals("chat") && jsonObject.has("content")) {
            if (jsonObject.opt("content") is String) {
                return jsonObject1.toString()
            }
            else {
                val obj: JSONObject = jsonObject.getJSONObject("content")
                jsonObject1.remove("content")
                jsonObject1.put("content", obj.opt("text"))
                if (obj.opt("attachment") is String) {
                } else {
                    if (obj.opt("attachment") is JSONObject) {
                    } else {
                        var arr: JSONArray = obj.getJSONArray("attachment")
                        if (arr.length() > 0) {
                            if (arr.get(0) is String) {
                                if (arr.get(0) is JSONObject) {
                                    if (arr.getJSONObject(0).has("path")) {
                                        jsonObject1.put(
                                            "attachment_url",
                                            arr.getJSONObject(0).opt("path")
                                        )
                                    }
                                } else {
                                    try {
                                        Gson().fromJson(arr.get(0).toString(), Any::class.java)
                                        val json = JSONObject(arr.get(0).toString())
                                        jsonObject1.put("attachment_url", json.opt("path"))
                                    } catch (ex: JsonSyntaxException) {
                                        jsonObject1.put("attachment_url", arr.get(0).toString())
                                    }
                                }
                            } else {
                                if (arr.getJSONObject(0).has("path"))
                                    jsonObject1.put(
                                        "attachment_url",
                                        arr.getJSONObject(0).opt("path")
                                    )
                                else if (arr.getJSONObject(0).has("second"))
                                    jsonObject1.put(
                                        "attachment_url",
                                        arr.getJSONObject(0).opt("second")
                                    )
                                else
                                    jsonObject1.put("attachment_url", arr.get(0).toString())
                            }
                        }
                    }
                }

                return jsonObject1.toString()
            }
        }
        else if(jsonObject.opt("type").equals("whatsapp")&& jsonObject.has("content")){
            val obj: JSONObject = jsonObject.getJSONObject("content")
            jsonObject1.remove("content")
            if (obj.has("text")) {
                jsonObject1.put("content", obj.opt("text"))
            } else if (obj.has("caption")) {
                jsonObject1.put("content", obj.opt("caption"))
            }else{
                jsonObject1.put("content", "")
            }
            if (obj.has("file_url"))
                jsonObject1.put("attachment_url", obj.opt("file_url"))
            else
                jsonObject1.put("attachment_url","")
            return jsonObject1.toString()
        } else if(jsonObject.opt("type").equals("Notes")||jsonObject.opt("type").equals("notes")||jsonObject.opt("type").equals("note") && jsonObject.has("content")){
            val obj: JSONObject = jsonObject.getJSONObject("content")
            jsonObject1.remove("content")
            if (obj.has("text")) {
                jsonObject1.put("content", obj.opt("text"))
            } else if (obj.has("caption")) {
                jsonObject1.put("content", obj.opt("caption"))
            }else{
                jsonObject1.put("content", "")
            }
            if (obj.has("file_url"))
                jsonObject1.put("attachment_url", obj.opt("file_url"))
            else
                jsonObject1.put("attachment_url","")
            return jsonObject1.toString()
        }
        else if (jsonObject.opt("type").equals("RCS")||jsonObject.opt("type").equals("rcs")&& jsonObject.has("content")) {
            val obj: JSONObject = jsonObject.getJSONObject("content")
            jsonObject1.remove("content")
            if (obj.has("text")) {
                jsonObject1.put("content", obj.opt("text"))
            } else if (obj.has("caption")) {
                jsonObject1.put("content", obj.opt("caption"))
            }else{
                jsonObject1.put("content", "")
            }
            if (obj.has("file_url"))
                jsonObject1.put("attachment_url", obj.opt("file_url"))
            else
                jsonObject1.put("attachment_url","")
            return jsonObject1.toString()
        } else {
            return jsonObject1.toString()
        }
    }

    fun removeSetting(){
        this.mPubnub_DataStream?.removeListener(chatCallback)
        this.mPubnub_DataStream?.unsubscribe()
            ?.channels(PUBSUB_CHANNEL)
            ?.execute()
    }

    fun sendTextMessage(content:String){
        if(content!=null) {
            var message = HashMap<String, String?>()
            message.put("content", content)
            message.put("type", "chat")
            //sendMessage(message)
        }
        else
            Log.d("MessageError","Can not send null")
    }
    fun sendImageMessage(content:String?, attachment:String) {
        if (attachment != null) {
            var message = HashMap<String, String?>()
            if (content != null)
                message.put("content", content)

            message.put("type", "chat")
                message.put("mime_type", "image/*")
                message.put("attachment_url", attachment)
            //sendMessage(message)

        } else
            Log.d("MessageError","Can not send null")
    }

    private fun sendMessage(message: HashMap<String, String?>) {

        mPubnub_DataStream?.publish()?.channel(PUBSUB_CHANNEL?.get(0))
//            .shouldStore()
            ?.message(message)?.async { result, status ->
                if (!status.isError) {
                    //                                editMessage.setText("")
                    pnSendMsgCallback?.callback()
                    Log.v(
                        "message1",
                        "publish(" + result?.timetoken + ")"
                    )
                } else {
                    Log.v(
                        "message1",
                        "publishErr(" + result?.timetoken + ")"
                    )
                }
            }
    }

    public fun sendSignalMessage(message: CallConnectionSignal, shouldStore:Boolean) {

        mPubnub_DataStream?.publish()?.channel(PUBSUB_CHANNEL?.get(0))
            ?.shouldStore(shouldStore)
            ?.message(message)?.async { result, status ->
                try {
                    if (!status.isError) {
                        //                                editMessage.setText("")
                        //                                pnSendMsgCallback?.callback()
                        Log.v(
                            "message1",
                            "publish(" + result?.timetoken + ")"
                        )
                    } else {
                        Log.v(
                            "message1",
                            "publishErr(" + result?.timetoken + ")"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    public fun reconnect(){
        this.mPubnub_DataStream?.reconnect()
    }


}
