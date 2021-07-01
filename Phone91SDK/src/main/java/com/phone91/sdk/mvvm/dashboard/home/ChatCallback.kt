package com.phone91.sdk.mvvm.dashboard.home

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.JsonNode
import com.google.gson.Gson
import com.phone91.sdk.model.CallConnectionSignal
import com.phone91.sdk.model.CallStatusSignal
import com.phone91.sdk.model.ChatObject
import com.phone91.sdk.utils.JsonUtil
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult

class ChatCallback(
    var chatAdapter: ChatAdapter,
    var rvPosts: RecyclerView
) : SubscribeCallback() {
    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
        Log.d("sgfjsgdhf",pnSignalResult.toString())
        val jsonMsg = pnSignalResult.message.asJsonObject
        pnSignalResult.message
        if(jsonMsg.has("callStatusChanged")){
            val callStatusSignal: CallStatusSignal = Gson().fromJson(jsonMsg,CallStatusSignal::class.java)//JsonUtil.convert(jsonMsg, CallStatusSignal::class.java)
            chatAdapter.callButtonStatus(callStatusSignal)
        }else if(jsonMsg.has("read")){
            chatAdapter.errorMessge( null)
        }
    }

    override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        Log.d("status",pnStatus.toString())
        if(pnStatus.category== PNStatusCategory.PNUnexpectedDisconnectCategory){
            if(pnStatus.errorData.throwable is PubNubException){
                var pubNubException=pnStatus.errorData.throwable as PubNubException
               chatAdapter.errorMessge( pubNubException.pubnubError.message)
            }
               // .pubnubError.message
        }else if(pnStatus.category== PNStatusCategory.PNReconnectedCategory){
            chatAdapter.errorMessge( null)
            // .pubnubError.message
        }
//        Log.d("status",pnStatus.toString())
    }

    override fun uuid(pubnub: PubNub, pnUUIDMetadataResult: PNUUIDMetadataResult) {
        Log.d("uuid",pnUUIDMetadataResult.toString())
    }

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
        Log.d("pubnub",pnMessageActionResult.toString())
    }

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
        Log.d("presence",pnPresenceEventResult.toString())
    }

    override fun channel(pubnub: PubNub, pnChannelMetadataResult: PNChannelMetadataResult) {
        Log.d("channel",pnChannelMetadataResult.toString())
    }

    override fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {
        Log.d("membership",pnMembershipResult.toString())
    }

    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
        Log.d("file",pnFileEventResult.toString())
    }

    override fun message(pubnub: PubNub, message: PNMessageResult) {
        val jsonMsg = message!!.message.asJsonObject

        Log.d("jsonMsg: signal",jsonMsg.toString())
        if(jsonMsg.has("callStatusChanged")){
            val callStatusSignal: CallStatusSignal = Gson().fromJson(jsonMsg,CallStatusSignal::class.java)//JsonUtil.convert(jsonMsg, CallStatusSignal::class.java)
            chatAdapter.callButtonStatus(callStatusSignal)
        }else if(message.publisher!=pubnub.configuration.uuid){
            if (jsonMsg.has("CallSignal") || jsonMsg.has("callSignal")) {
                val callConnectionSignal: CallConnectionSignal =
                    Gson().fromJson(jsonMsg, CallConnectionSignal::class.java)
                callConnectionSignal.time = message!!.timetoken
                chatAdapter.callConnectionStatus(callConnectionSignal)
            } else if (jsonMsg.has("roomUrl")) {
                val callConnectionSignal: CallConnectionSignal =
                    Gson().fromJson(jsonMsg, CallConnectionSignal::class.java)
                callConnectionSignal.time = message!!.timetoken
                callConnectionSignal.CallSignal = CallConnectionSignal.ROOM_SIGNAL
                chatAdapter.callConnectionStatus(callConnectionSignal)
            }else {
                val callConnectionSignal: CallConnectionSignal =
                    Gson().fromJson(jsonMsg, CallConnectionSignal::class.java)
                callConnectionSignal.time = message!!.timetoken
                if (callConnectionSignal.type.equals("chat")) {

                    chatAdapter.add(callConnectionSignal)
                    rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }

                //            val chatObject: ChatObject = JsonUtil.convert(jsonMsg, ChatObject::class.java)
                //
                //
                //           if(chatObject.type.equals("chat")) {
                //               chatObject.time = message!!.timetoken
                //               chatAdapter.add(chatObject)
                //               rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
                //           }
            }
        } else {
            val callConnectionSignal: CallConnectionSignal =
                Gson().fromJson(jsonMsg, CallConnectionSignal::class.java)
            callConnectionSignal.time = message!!.timetoken
            if (callConnectionSignal.type.equals("chat")) {

                chatAdapter.add(callConnectionSignal)
                rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }else if(callConnectionSignal.CallSignal==CallConnectionSignal.CALL_STARTED || callConnectionSignal.CallSignal==CallConnectionSignal.CALL_END
                || callConnectionSignal.CallSignal==CallConnectionSignal.AUDIO_JOIN_CALL_TIMEOUT || callConnectionSignal.CallSignal==CallConnectionSignal.VIDEO_JOIN_CALL_TIMEOUT)
                chatAdapter.callConnectionStatus(callConnectionSignal)

            //            val chatObject: ChatObject = JsonUtil.convert(jsonMsg, ChatObject::class.java)
            //
            //
            //           if(chatObject.type.equals("chat")) {
            //               chatObject.time = message!!.timetoken
            //               chatAdapter.add(chatObject)
            //               rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
            //           }
        }

    }

}

    /*  override fun status(pubnub: PubNub?, status: PNStatus?) {
      }

      override fun presence(pubnub: PubNub?, presence: PNPresenceEventResult?) {
      }

      override fun message(pubnub: PubNub?, message: PNMessageResult?) {
          try {


  //            Log.v(
  //                "",
  //                "message(" + JsonUtil.asJson(message).toString() + ")"
  //            )


              val jsonMsg: JsonNode = message!!.message

              if(jsonMsg.has("callStatusChanged")){
                  val callStatusSignal: CallStatusSignal = JsonUtil.convert(jsonMsg, CallStatusSignal::class.java)
                  chatAdapter.callButtonStatus(callStatusSignal)
              }else if(jsonMsg.has("CallSignal") || jsonMsg.has("callSignal")){
                  val callConnectionSignal: CallConnectionSignal = JsonUtil.convert(jsonMsg, CallConnectionSignal::class.java)
                  chatAdapter.callConnectionStatus(callConnectionSignal)
              }else if(jsonMsg.has("roomUrl")) {
                  val callConnectionSignal: CallConnectionSignal = JsonUtil.convert(jsonMsg, CallConnectionSignal::class.java)
                  callConnectionSignal.CallSignal=CallConnectionSignal.ROOM_SIGNAL
                  chatAdapter.callConnectionStatus(callConnectionSignal)
              }else{
                  val callConnectionSignal: CallConnectionSignal = JsonUtil.convert(jsonMsg, CallConnectionSignal::class.java)

                  if(callConnectionSignal.type.equals("chat")) {
                      callConnectionSignal.time = message!!.timetoken
                   chatAdapter.add(callConnectionSignal)
                 rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
             }

  //            val chatObject: ChatObject = JsonUtil.convert(jsonMsg, ChatObject::class.java)
  //
  //
  //           if(chatObject.type.equals("chat")) {
  //               chatObject.time = message!!.timetoken
  //               chatAdapter.add(chatObject)
  //               rvPosts.smoothScrollToPosition(chatAdapter.itemCount - 1)
  //           }
          }

          } catch (e: Exception) {
              e.printStackTrace()
          }

      }*/

