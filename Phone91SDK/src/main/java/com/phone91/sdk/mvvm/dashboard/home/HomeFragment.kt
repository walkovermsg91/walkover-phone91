package com.phone91.sdk.mvvm.dashboard.home


//import com.phone91.sdk.mvvm.dashboard.postdetails.PostDetailsFragment
//import kotlinx.android.synthetic.main.fragment_home_new.*

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import com.phone91.sdk.R
import com.phone91.sdk.model.CallConnectionSignal
import com.phone91.sdk.model.CallStatusSignal
import com.phone91.sdk.model.WidgetObject
import com.phone91.sdk.mvvm.base.BaseFragment
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
import com.phone91.sdk.mvvm.pubnub.PNSendMsgCallback
import com.phone91.sdk.mvvm.pubnub.PubnubSetting
import com.phone91.sdk.utils.PathUtil
import com.phone91.sdk.utils.toolbar.FragmentToolbar
import com.scottyab.aescrypt.AESCrypt
import kotlinx.android.synthetic.main.fragment_home_new.*
import okhttp3.internal.notify
import java.io.*
import java.nio.charset.Charset
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment<HomeViewModel>(), PNSendMsgCallback, ChatAdapter.CallbackClient {


    lateinit  var  ringtone: Ringtone
    var connectedCall: CallConnectionSignal?=null
    private lateinit var progressDialog: Dialog
    private  var pubnubSetting: PubnubSetting?=null
    public  var isFirst: Boolean=false
    private  var isConnected: Boolean=false
    var rvPost:RecyclerView?=null
    var llPanelCall:LinearLayout?=null
    var llPanel:LinearLayout?=null

//    @Inject
//    lateinit var appViewModelFactory: AppViewModelFactory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var chatAdapter: ChatAdapter
//    @Inject

    companion object {
        //        @JvmStatic
        fun newInstance() = HomeFragment()


        var widgetObject: WidgetObject? = null

        //
        @JvmStatic
        fun newInstance(widgetObject: WidgetObject): HomeFragment {
            this.widgetObject = widgetObject
            var fragment = HomeFragment()
            return fragment
        }

    }

    override fun builder(): FragmentToolbar? {

        return FragmentToolbar.Builder()
                .withId(R.id.toolBarL)
                .withTitle(widgetObject?.name!!)
//            .withTitle(if(widgetObject?.name!=null && !widgetObject?.name.equals("")) widgetObject?.name!! else "Support")
                .withDescription(widgetObject?.tagline!!)
                .withClose()
                .build()
    }



    override fun getFragmentTAG(): FragmentTAG = FragmentTAG.HOME_FRAGMENT

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this@HomeFragment)
                .get(HomeViewModel::class.java)
        homeViewModel.setAppDataManager((context as DashboardActivity).application)
        return homeViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home_new, container, false)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//           homeViewModel.getNameByChannel(appDataManager.getChannel()!!)

//        PUBSUB_CHANNEL =
//            Arrays.asList<String>(appDataManager.getChannel())

        progressDialog = Dialog(activity!!)
        progressDialog.setCancelable(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setContentView(R.layout.dialog_progress)

        rvPost= view .findViewById(R.id.rvPosts) as RecyclerView
        llPanel= view .findViewById(R.id.llPanel) as LinearLayout
        llPanelCall= view .findViewById(R.id.llPanelCall) as LinearLayout

        if((activity as DashboardActivity).pos!=-1)
            txtName.text= widgetObject?.teams?.get((activity as DashboardActivity).pos)?.name
        else {
            imgBack.visibility = View.GONE
        }

        chatAdapter=ChatAdapter(activity, ArrayList(), this)
//        teamAdapter.setOnItemSelectionListener(this)
        var layoutManager = LinearLayoutManager(activity as DashboardActivity, RecyclerView.VERTICAL, false)
        rvPost?.layoutManager=layoutManager
        rvPost?.apply {
            adapter = chatAdapter
        }
        ringtone =  context!!.defaultRingtone
//        val mediaPlayer:MediaPlayer = context!!.defaultRingtonePlayer()

//        initPubNub()

//        if(appDataManager.getChannel()!=null)
//            callPubnubSetting()
//        else
        if(DashboardActivity.isCallEnable && widgetObject?.enable_call!! && pubnubSetting!=null)
            //imgcall.visibility=View.VISIBLE
            imgcall.visibility=View.GONE
        else
            imgcall.visibility=View.GONE

        (activity as DashboardActivity).checkChannelDetailFromChat()

        observeError()
        observeLoading()
        observeSuccess()

//        if(pubnubSetting!=null)
//
//            pubnubSetting?.sendTextMessage(editMessage.text.toString().trim())
//        else{
//            isFirst=true
//            (activity as DashboardActivity).createChannel()
//        }


        imgSend.setOnClickListener {
            if (!editMessage.text.toString().trim().equals("")) {
                if(isNetworkConnected()) {
                    if (pubnubSetting != null)
                        //pubnubSetting?.sendTextMessage(editMessage.text.toString().trim())
                        homeViewModel.sendTextMSG(editMessage.text.toString().trim())
                    else {
                        isFirst = true
                        var connectionSignal = CallConnectionSignal()
                        connectionSignal.content = editMessage.text.toString().trim()
//                    connectionSignal.time=Date().time
                        connectionSignal.type = "chat"
                        chatAdapter.add(connectionSignal)
                        editMessage.setText("")
                        (activity as DashboardActivity).createChannel()
                    }
                }else{
                    showError("Make sure your phone has an internet connection and try again")
                }

            }

        }

        if(pubnubSetting==null) {
            attachment.isEnabled = false
            attachment.setColorFilter(ContextCompat.getColor(context!!, R.color.arrow_color), android.graphics.PorterDuff.Mode.SRC_IN);

        }

        imgcall.setOnClickListener {

            if(isNetworkConnected()) {
                openAudioVideoDaiog("")
            }else{
                showError("Make sure your phone has an internet connection and try again")
            }



        }

        btnCancel.setOnClickListener {

//            if(appDataManager.getCallId()!=null || appDataManager.getMsg91DeviceId()!=null){
//                var callConnectionSignal = CallConnectionSignal()

            connectedCall?.type="CALL"
//                callConnectionSignal.WidgetDeviceId=appDataManager.getWidgetDeviceId()
            connectedCall?.CallSignal= if(connectedCall?.CallType==CallConnectionSignal.AUDIO) CallConnectionSignal.AUDIO_JOIN_CALL_REJECT
            else CallConnectionSignal.VIDEO_JOIN_CALL_REJECT
//                var json= Gson().toJson(callConnectionSignal)
            pubnubSetting?.sendSignalMessage(connectedCall!!,false)
//            }
            if(ringtone.isPlaying)
                ringtone.stop()
            isConnected=true
            removeAll()
            countDownTimer.cancel()
            llPanel?.visibility=View.GONE
            connectedCall=null;


        }

        btnAnswer.setOnClickListener {

//            if(appDataManager.getCallId()!=null || appDataManager.getMsg91DeviceId()!=null){
//                var callConnectionSignal = CallConnectionSignal()

//                appDataManager.setCallId(connectedCall?.CallId!!)
//                appDataManager.setMsg91DeviceId(connectedCall?.Msg91DeviceId!!)
//                connectedCall?.type="CALL"
////                callConnectionSignal.WidgetDeviceId=appDataManager.getWidgetDeviceId()
//                connectedCall?.CallSignal= if(connectedCall?.CallType==CallConnectionSignal.AUDIO) CallConnectionSignal.AUDIO_JOIN_CALL_ACCEPT else CallConnectionSignal.VIDEO_JOIN_CALL_ACCEPT
////                var json= Gson().toJson(callConnectionSignal)
//                pubnubSetting?.sendSignalMessage(connectedCall!!,true)
//            }
            if(ringtone.isPlaying)
                ringtone.stop()
            isConnected=true
            countDownTimer.cancel()
            llPanel?.visibility=View.GONE
//             var roomid=connectedCall?.roomUrl
//            (activity as DashboardActivity).initiateCall(roomid?.replace("https://marlin.phone91.com/","")!!,
//                connectedCall?.CallType==CallConnectionSignal.AUDIO
//            )

            parseUrl()

        }




        imgBack.setOnClickListener{
            if(pubnubSetting!=null)
                pubnubSetting?.removeSetting()
            activity?.supportFragmentManager?.popBackStack()
        }



        attachment.setOnClickListener{
            if(isNetworkConnected()) {
                if (checkPermission())
                    openCameraAndGalley()
            }else{
                showError("Make sure your phone has an internet connection and try again")
            }

        }





    }


//    override fun onDestroy() {
//        super.onDestroy()
//        if(pubnubSetting!=null)
//            pubnubSetting?.removeSetting()
//    }


    public fun callPubnubSetting(){
        if(DashboardActivity.isCallEnable && widgetObject?.enable_call!!) {
            //imgcall.visibility=View.VISIBLE
            imgcall.visibility = View.GONE
        }else
            imgcall.visibility=View.GONE
        pubnubSetting= PubnubSetting()
        pubnubSetting?.setChannel(appDataManager?.getChannel()!!)
        pubnubSetting?.initPubNub(appDataManager!!)
        var msg:String?=null
        if(chatAdapter.teamList.size>0)
            msg=chatAdapter.teamList.get(0).content
        chatAdapter.teamList.clear()
        chatAdapter.notifyDataSetChanged()
        pubnubSetting?.initChannels(chatAdapter, rvPost!!,this)

        if(isFirst) {
            isFirst=false
//            pubnubSetting?.sendTextMessage(editMessage.text.toString().trim())
            //pubnubSetting?.sendTextMessage(msg!!)
            homeViewModel.sendTextMSG(msg!!)

        }
        if(pubnubSetting!=null)
        {
            attachment.isEnabled=true
            attachment.setColorFilter(ContextCompat.getColor(context!!, R.color.chat_txt), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 88
    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        // request camera permission if it has not been grunted.
        if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.CAMERA
                ) !== android.content.pm.PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== android.content.pm.PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ), CAMERA_PERMISSION_REQUEST_CODE
            )
            return false
        } else
            return true
    }


    val OPEN_CAMERA = 2
    val OPEN_GALLARY = 3
    private var selectedImage: String? = null
    private fun openCameraAndGalley() {

        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.select_option_new)

        val select_from_gallery = dialog.findViewById(R.id.select_from_gallery) as Button
        val select_from_camera = dialog.findViewById(R.id.select_from_camera) as Button
        val cancel = dialog.findViewById(R.id.cancel) as Button

        select_from_gallery.setOnClickListener(View.OnClickListener {
            val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                    pickPhoto,
                    OPEN_GALLARY
            )//one can be replaced with any action code
            dialog.dismiss()
        })

        select_from_camera.setOnClickListener(View.OnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, OPEN_CAMERA)
            dialog.dismiss()
        })

        cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun observeError() {
        homeViewModel.error.observeForever {
            showError(it!!)
        }
    }

    private fun showError(error: String) {
        (activity as DashboardActivity).showErrorToast(error)
    }

    private fun observeLoading() {

        homeViewModel.loading.observeForever {
            showProgressLoader(it!!)
        }
    }

    private fun showProgressLoader(visible: Boolean) {
        if (visible) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    private fun observeSuccess() {
        homeViewModel.roomData.observeForever {
            if (it!=null) {
                connectedCall?.type="CALL"
//                callConnectionSignal.WidgetDeviceId=appDataManager.getWidgetDeviceId()
                connectedCall?.CallSignal= CallConnectionSignal.CALL_STARTED
                connectedCall?.msg= "Call has been started"
//                var json= Gson().toJson(callConnectionSignal)
                pubnubSetting?.sendSignalMessage(connectedCall!!,true)

                (activity as DashboardActivity).initiateCall(it.room!!,connectedCall?.CallType==CallConnectionSignal.AUDIO, it.jwt!!)


            }

        }
        homeViewModel.sendData.observeForever {
            editMessage.setText("")
        }


        homeViewModel.urlData.observeForever {

            if(!editMessage.text.toString().trim().equals("")) {
                //pubnubSetting?.sendImageMessage(editMessage.text.toString().trim(), it)
                homeViewModel?.sendImageMessage(
                    editMessage.text.toString().trim(),
                    "text-attachment",
                    it
                )
            }else {
                //pubnubSetting?.sendImageMessage(null, it)
                homeViewModel?.sendImageMessage("", "attachment", it)
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                OPEN_GALLARY -> {
                    selectedImage = PathUtil.getPath(activity, data?.data)

                }
                OPEN_CAMERA -> {
                    captureImageResult(data)
                }
            }

            if (filteExtension(selectedImage!!)) {
                homeViewModel.shareFile(selectedImage!!)
            }else {
                showError("Selected File Not Allowed")
                Log.e("fileExtension", "fileExtension false")
            }

        }
    }
    private fun filteExtension(image: String): Boolean {
        val file = File(image)
        val extension =
            arrayOf("aif", "cda", "mid", "midi", "mp3", "mpa", "ogg","oga","ogv","ogx","wav", "wma",
                "wpl", "7z", "arj", "deb", "pkg", "rar", "rpm", "tar", "gz", "z", "zip", "dmg",
                "iso", "vcd", "csv", "xml","email", "eml", "emlx", "msg", "oft", "ost", "pst", "vcf",
                "fnt", "fon", "otf", "ttf", "ai","bmp", "gif", "ico", "jpeg", "jpg", "png", "ps", "psd",
                "svg", "tif", "tiff", "cer", "cfm","html","ods", "xls", "xlsm", "xlsx", "3g2", "3gp",
                "avi", "flv", "h264", "m4v", "mkv", "mov", "mp4", "mpg", "mpeg", "rm", "swf", "vob", "wmv",
                "doc", "docx", "odt", "pdf", "rtf", "tex", "txt", "wpd", "ppt", "pptx", "ppt", "json")
        for (ext in extension) {
            if (file.extension.equals(ext)) {
                return true
            }
        }
        return false
    }
    private fun captureImageResult(/*selectedImage: Uri?, */data: Intent?) {
        val thumbnail = data?.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination = File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var uri = Uri.fromFile(destination);
        selectedImage = uri.path
    }

    override fun callback() {
        editMessage.setText("")

    }

    override fun onsetClient(name: String, email: String, phone: String) {
        if(pubnubSetting!=null)
            pubnubSetting?.removeSetting()

        (activity as DashboardActivity).setClient(appDataManager?.getChannel(),name, email, phone,appDataManager?.getUUID())
    }

    override fun callList(name: String, email: String, phone: String) {
        if(pubnubSetting!=null)
            pubnubSetting?.removeSetting()
        activity?.runOnUiThread{
            (activity as DashboardActivity).getChannelList1(name, email, phone)
        }

    }

    override fun callButtonStatusListener(callStatusSignal: CallStatusSignal) {
        runOnUiThread {
            if(callStatusSignal.callStatusChanged==CallStatusSignal.ENABLE && widgetObject?.enable_call!! && pubnubSetting!=null)
                //imgcall.visibility=View.VISIBLE
                imgcall.visibility=View.GONE
            else
                imgcall.visibility=View.GONE
            // returns an object which
            // is a Runnable, but does
        }

    }

    @SuppressLint("NewApi")
    override fun callConnectionListener(callConnectionSignal: CallConnectionSignal) {
        when(callConnectionSignal.CallSignal){
            CallConnectionSignal.PERMIT_CALL_AUDIO->{
//                      widgetDeviceId = generate UUID which start with 'UUIDV4';


//                      check whether android widget has saved call id or msgdeviceId. if it has one of them then send callsignal ''CALL-BUSY'

                // save here call id, messagedevice id

                if(appDataManager?.getCallId()!=null || appDataManager?.getMsg91DeviceId()!=null) {
                    callConnectionSignal.type="CALL"
                    callConnectionSignal.CallSignal=CallConnectionSignal.CALL_BUSY
                    pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
                }else{
                    var widgetDeviceId=/*CallConnectionSignal.PREFIX_widgetDeviceId+ */UUID.randomUUID().toString()

                    appDataManager?.setWidgetDeviceId(widgetDeviceId)
                    appDataManager?.setCallId(callConnectionSignal.CallId)
                    appDataManager?.setMsg91DeviceId(callConnectionSignal.Msg91DeviceId)
                    callConnectionSignal.type="CALL"
                    callConnectionSignal.WidgetDeviceId=appDataManager?.getWidgetDeviceId()
                    callConnectionSignal.CallSignal=CallConnectionSignal.ACCEPT_PERMIT_AUDIO
                    pubnubSetting?.sendSignalMessage(callConnectionSignal,false)

                }



            }
            CallConnectionSignal.PERMIT_CALL_VIDEO->{

                // save here call id, messagedevice id
                if(appDataManager?.getCallId()!=null || appDataManager?.getMsg91DeviceId()!=null) {
                    callConnectionSignal.type="CALL"
                    callConnectionSignal.CallSignal=CallConnectionSignal.CALL_BUSY
                    pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
                }else{
                    var widgetDeviceId=/*CallConnectionSignal.PREFIX_widgetDeviceId+ */UUID.randomUUID().toString()

                    appDataManager?.setWidgetDeviceId(widgetDeviceId)
                    callConnectionSignal.type="CALL"
                    callConnectionSignal.WidgetDeviceId=appDataManager?.getWidgetDeviceId()
                    appDataManager?.setCallId(callConnectionSignal.CallId)
                    appDataManager?.setMsg91DeviceId(callConnectionSignal.Msg91DeviceId)
                    callConnectionSignal.CallSignal=CallConnectionSignal.ACCEPT_PERMIT_VIDEO
                    pubnubSetting?.sendSignalMessage(callConnectionSignal,false)



                }
            }
            CallConnectionSignal.ACCEPT_PERMIT_AUDIO->{

                callConnectionSignal.type="CALL"
                if(appDataManager?.getMsg91DeviceId()==null) {
                    callConnectionSignal.CallSignal = CallConnectionSignal.INIT_CALL_AUDIO

                }else
                    callConnectionSignal.CallSignal=CallConnectionSignal.CALL_ASSIGNED

                pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
                isConnected=true

                runOnUiThread{
                    llPanelCall?.visibility = View.VISIBLE

                    txtMSG.text=getString(R.string.agent_crt_room)
                    if(countDownTimer!=null)
                        countDownTimer.cancel()
                }
            }
            CallConnectionSignal.ACCEPT_PERMIT_VIDEO->{
                callConnectionSignal.type="CALL"
                if(appDataManager?.getMsg91DeviceId()==null) {
                    callConnectionSignal.CallSignal = CallConnectionSignal.INIT_CALL_VIDEO

                }
                else
                    callConnectionSignal.CallSignal=CallConnectionSignal.CALL_ASSIGNED

                pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
                isConnected=true
                runOnUiThread{
                    llPanelCall?.visibility = View.VISIBLE
                    txtMSG.text=getString(R.string.agent_crt_room)
                    if(countDownTimer!=null)
                        countDownTimer.cancel()
                }
            }
            CallConnectionSignal.DECLINE_PERMIT_AUDIO->{
                removeAll()
                isConnected=true
                countDownTimer.cancel()
                runOnUiThread{
                    llPanelCall?.visibility=View.GONE
                    Toast.makeText(context,"Agent has declined your call request",Toast.LENGTH_SHORT).show()
                }
            }
            CallConnectionSignal.DECLINE_PERMIT_VIDEO->{
                removeAll()
                isConnected=true
                countDownTimer.cancel()
                runOnUiThread{
                    llPanelCall?.visibility=View.GONE
                    Toast.makeText(context,"Agent has declined your call request",Toast.LENGTH_SHORT).show()
                }
            }
            CallConnectionSignal.INIT_CALL_AUDIO->{
                if(callConnectionSignal.CallId!=null && !callConnectionSignal.CallId?.startsWith("w-")!!) {
                    callConnectionSignal.type = "CALL"
                    callConnectionSignal.CallSignal = CallConnectionSignal.INIT_CALL_AUDIO
                    pubnubSetting?.sendSignalMessage(callConnectionSignal, false)
                }
            }
            CallConnectionSignal.INIT_CALL_VIDEO->{

                if(callConnectionSignal.CallId!=null && !callConnectionSignal.CallId?.startsWith("w-")!!) {
                    callConnectionSignal.type = "CALL"
                    callConnectionSignal.CallSignal = CallConnectionSignal.INIT_CALL_VIDEO
                    pubnubSetting?.sendSignalMessage(callConnectionSignal, false)
                }
//                      }
            }
            CallConnectionSignal.END_CALL_AUDIO->{}
            CallConnectionSignal.END_CALL_VIDEO->{}
            CallConnectionSignal.CALL_BUSY->{
                removeAll()
                isConnected=true
                countDownTimer.cancel()
                runOnUiThread{
                    llPanelCall?.visibility=View.GONE
                    if(context!=null)
                        Toast.makeText(context,"Agent is busy",Toast.LENGTH_SHORT).show()
                }

            }
            CallConnectionSignal.CALL_ASSIGNED->{}
            CallConnectionSignal.CALL_STARTED->{
                chatAdapter.add(callConnectionSignal)
                rvPost?.smoothScrollToPosition(chatAdapter.itemCount - 1)

            }
            CallConnectionSignal.VIDEO_JOIN_CALL_TIMEOUT->{
                if(callConnectionSignal.CallId!=null && callConnectionSignal.CallId?.startsWith("w-")!!)
                    callConnectionSignal.msg="Your video call did not connect"
                else
                    callConnectionSignal.msg="You missed the video call from agent"

                chatAdapter.add(callConnectionSignal)
                rvPost?.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }
            CallConnectionSignal.AUDIO_JOIN_CALL_TIMEOUT->{
                if(callConnectionSignal.CallId!=null && callConnectionSignal.CallId?.startsWith("w-")!!)
                    callConnectionSignal.msg="Your audio call did not connect"
                else
                    callConnectionSignal.msg="You missed the audio call from agent"
                chatAdapter.add(callConnectionSignal)
                rvPost?.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }

            CallConnectionSignal.VIDEO_JOIN_CALL_ACCEPT->{}
            CallConnectionSignal.AUDIO_JOIN_CALL_ACCEPT->{}
            CallConnectionSignal.VIDEO_JOIN_CALL_REJECT->{}
            CallConnectionSignal.AUDIO_JOIN_CALL_REJECT->{}
            CallConnectionSignal.MISS_CALL_AUDIO->{}
            CallConnectionSignal.MISS_CALL_VIDEO->{}
            CallConnectionSignal.CALL_END->{
                removeAll()
                chatAdapter.add(callConnectionSignal)
                rvPost?.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }
            CallConnectionSignal.ROOM_SIGNAL->{

                if(callConnectionSignal.CallId!=null && !callConnectionSignal?.CallId?.startsWith("w-")!! && callConnectionSignal.type?.equals("join-room")!! ){
                    connectedCall=callConnectionSignal

                    runOnUiThread{
                        if (!ringtone.isPlaying){
                            ringtone.isLooping=true
                            ringtone.play()
                        }

                        llPanel?.visibility=View.VISIBLE
                        countDownTimer.start()
                    }

                }else if(callConnectionSignal.CallId!=null && callConnectionSignal?.CallId?.startsWith("w-")!! && callConnectionSignal.type?.equals("join-room")!! ){

                    connectedCall= callConnectionSignal
                    parseUrl()

                }


            }

        }
    }

    override fun connectionErrorListener(massage: String?) {
        if(massage==null)
            pubnubSetting?.reconnect()
        else {

            showError(massage)
        }
    }

    fun parseUrl(){
        connectedCall?.type = "CALL"
        if(connectedCall?.CallType==CallConnectionSignal.AUDIO)
            connectedCall?.CallSignal = CallConnectionSignal.AUDIO_JOIN_CALL_ACCEPT
        else
            connectedCall?.CallSignal = CallConnectionSignal.VIDEO_JOIN_CALL_ACCEPT

        pubnubSetting?.sendSignalMessage(connectedCall!!, false)
        runOnUiThread{

            llPanelCall?.visibility=View.GONE
            homeViewModel.callAPI(appDataManager?.getChatId()!!)
        }
    }


    fun decrypt(
            cipherText: String?
    ): String? {
        try {
//            9bf8453f

//            var decodedKey: ByteArray?=null
//            if(Build.VERSION.SDK_INT>=26)
//               decodedKey = Base64.getDecoder().decode("9bf8453f")
//            else

//              var decodedKey = SecretKeyFactory.getInstance("AES").generateSecret(
//                  PBEKeySpec("9bf8453f".toCharArray())).encoded

//            val keyGenerator: KeyGenerator = KeyGenerator.getInstance(
//                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
//            )
//
//            keyGenerator.init(
//                Builder(
//                    "key2",
//                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                )
//                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//                    .build()
//            )

//            val keyStore: KeyStore = KeyStore.getInstance("AES")
////            keyStore.load(null)
//            var key = keyStore.getKey("key2", "9bf8453f".toCharArray()) as SecretKey




            //"9bf8453f".toByteArray() //android.util.Base64.decode("9bf8453f", android.util.Base64.DEFAULT)

            /*val decodedKey = "9bf8453f".toByteArray()if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getDecoder().decode("9bf8453f")
            } else {
                android.util.Base64.decode("9bf8453f", android.util.Base64.DEFAULT) // Unresolved reference: decode
            }*/
// rebuild key using SecretKeySpec
// rebuild key using SecretKeySpec
//            val key: SecretKey =
//                SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")


//            var pbeKey =  PBEKeySpec("9bf8453f".toCharArray());
//            var keyFactory = SecretKeyFactory.getInstance("AES");
//            var secretKey = keyFactory.generateSecret(pbeKey);

//            val IV = ByteArray(16)
//            val random: SecureRandom
//            random = SecureRandom()
//            random.nextBytes(IV)
//            val cipher: Cipher = Cipher.getInstance("AES")
////            val keySpec = SecretKeySpec(secretKey.getEncoded(), "AES")
//            val ivSpec = IvParameterSpec(IV)
//            cipher.init(Cipher.DECRYPT_MODE,key , ivSpec)
//            val decryptedText: ByteArray = cipher.doFinal(cipherText)


            var str="9bf8453f"
//            val skeySpec =
//                SecretKeySpec(str.toByteArray(), "AES")
//            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
//            val decryptedText: ByteArray= cipher.doFinal(cipherText)
            try {

                val messageAfterDecrypt: String = AESCrypt.decrypt(str, cipherText)
                if(messageAfterDecrypt!=null) {
                    Log.d("gdjasghd", messageAfterDecrypt)
                    return messageAfterDecrypt
                }
            } catch (e: GeneralSecurityException) {
                //handle error - could be due to incorrect password or tampered encryptedMsg
                Log.d("error",e.message)
            }




            return null//String(decryptedText,Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

//    val PROVIDER = "BC"
//    val KEY_SPEC_ALGORITHM = "AES"
//    val CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"
//    @Throws(java.lang.Exception::class)
//    fun decode(str :String,cipherText: String?): ByteArray? {
//        val encodedKey: ByteArray = Base64.decode(
//            str,
//            Base64.DEFAULT
//        )
//        val secretKeySpec = SecretKeySpec(
//            encodedKey,
//            0,
//            encodedKey.size,
//            KEY_SPEC_ALGORITHM
//        )
//        val cipher: Cipher = Cipher.getInstance(
//            CIPHER_ALGORITHM,
//            PROVIDER
//        )
////        val secureRandom = SecureRandom()
////        secureRandom.setSeed(
////            MYTESTKEY.toByteArray(
////                Charset.forName(
////                    "UTF-8"
////                )
////            )
////        )
//        cipher.init(
//            Cipher.DECRYPT_MODE, secretKeySpec,
//            IvParameterSpec(ByteArray(cipher.blockSize))
//        )
//        return cipher.doFinal(fileData)
//    }

    @Throws(java.lang.Exception::class)
    private fun dataFromHexString(hexString: String): ByteArray? {
        var hexString = hexString
        hexString = hexString.trim { it <= ' ' }
        hexString = hexString.replace("[ ]".toRegex(), "")
        hexString = hexString.toLowerCase()
        val validHexChar = "abcdef0123456789"
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            val c1 = hexString[i]
            val c2 = hexString[i + 1]
            if (!validHexChar.contains("" + c1) || !validHexChar.contains("" + c2)) {
                throw java.lang.Exception("Invalid encryption hex data")
            }
            data[i / 2] =
                    ((Character.digit(c1, 16) shl 4) + Character.digit(
                            c2,
                            16
                    )).toByte()
            i += 2
        }
        return data
    }

    var countDownTimer= object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d("onTick","onTick")
            if(!isNetworkConnected()) {
                Log.d("onTick1","onTick1")
                cancel()
                onFinish()
            }

        }


        override fun onFinish() {
            Log.d("onFinish","onFinish")
            runOnUiThread{
                Log.d("onFinish1","onFinish1")
                if (ringtone.isPlaying) {
                    ringtone.stop()
                }

                llPanel?.visibility = View.GONE
                llPanelCall?.visibility = View.GONE
                if(!isConnected && isNetworkConnected()) {


                    connectedCall?.type = "CALL"
                    connectedCall?.CallSignal =
                            if (connectedCall?.CallType == CallConnectionSignal.AUDIO) CallConnectionSignal.AUDIO_JOIN_CALL_TIMEOUT else CallConnectionSignal.VIDEO_JOIN_CALL_TIMEOUT

                    pubnubSetting?.sendSignalMessage(connectedCall!!, true)

                    removeAll()
                }
                isConnected=false

            }


        }
    }
    // Extension property to get default ringtone
    val Context.defaultRingtone:Ringtone
        get() {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            return RingtoneManager.getRingtone(this, uri)
        }


    // Extension function to get a media player to play default ringtone
    fun Context.defaultRingtonePlayer(): MediaPlayer {
        return MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
    }


    private fun openAudioVideoDaiog(roomId:String) {

        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.select_option_new)

        val select_from_gallery = dialog.findViewById(R.id.select_from_gallery) as Button
        val select_from_camera = dialog.findViewById(R.id.select_from_camera) as Button
        val cancel = dialog.findViewById(R.id.cancel) as Button

        select_from_gallery.text="Initaite Video call"
        select_from_camera.text="Initaite Audio call"

        select_from_gallery.setOnClickListener(View.OnClickListener {
//            (activity as DashboardActivity).initiateCall("",false)
            var callConnectionSignal= CallConnectionSignal()
            var widgetDeviceId=/*CallConnectionSignal.PREFIX_widgetDeviceId+ */UUID.randomUUID().toString()
            var callId="w-"+ UUID.randomUUID().toString()

            appDataManager?.setWidgetDeviceId(widgetDeviceId)

            callConnectionSignal.type="CALL"
            callConnectionSignal.CallType=CallConnectionSignal.VIDEO
            callConnectionSignal.WidgetDeviceId=appDataManager?.getWidgetDeviceId()
            callConnectionSignal.widgetToken=appDataManager?.getWidgetToken()
            callConnectionSignal.CallId=callId
            callConnectionSignal.CallSignal=CallConnectionSignal.PERMIT_CALL_VIDEO
            connectedCall=callConnectionSignal
            pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
            isConnected=false
            countDownTimer.start()

            runOnUiThread{
                llPanelCall?.visibility = View.VISIBLE
                txtMSG.text=getString(R.string.agent_wait)
            }
            dialog.dismiss()
        })

        select_from_camera.setOnClickListener(View.OnClickListener {
//            (activity as DashboardActivity).initiateCall("")
            var callConnectionSignal= CallConnectionSignal()
            var widgetDeviceId=/*CallConnectionSignal.PREFIX_widgetDeviceId+ */UUID.randomUUID().toString()
            var callId="w-"+ UUID.randomUUID().toString()

            appDataManager?.setWidgetDeviceId(widgetDeviceId)

            callConnectionSignal.type="CALL"
            callConnectionSignal.CallType=CallConnectionSignal.AUDIO
            callConnectionSignal.WidgetDeviceId=appDataManager?.getWidgetDeviceId()
            callConnectionSignal.widgetToken=appDataManager?.getWidgetToken()
            callConnectionSignal.CallId=callId
            connectedCall=callConnectionSignal
            callConnectionSignal.CallSignal=CallConnectionSignal.PERMIT_CALL_AUDIO
            pubnubSetting?.sendSignalMessage(callConnectionSignal,false)
            isConnected=false
            countDownTimer.start()
            runOnUiThread{
                llPanelCall?.visibility = View.VISIBLE
                txtMSG.text=getString(R.string.agent_wait)
            }
            dialog.dismiss()
        })

        cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    fun callDone() {
        connectedCall?.type="CALL"
        connectedCall?.CallSignal=if(connectedCall?.CallType==CallConnectionSignal.AUDIO) CallConnectionSignal.END_CALL_AUDIO else CallConnectionSignal.END_CALL_VIDEO
        pubnubSetting?.sendSignalMessage(connectedCall!!,false)


        connectedCall?.type="CALL"
        connectedCall?.CallSignal=CallConnectionSignal.CALL_END
        connectedCall?.msg="Call has been ended"
        connectedCall?.sender_id="client"
        pubnubSetting?.sendSignalMessage(connectedCall!!,true)
    }

    fun removeAll(){

        appDataManager?.setMsg91DeviceId(null)
        appDataManager?.setWidgetDeviceId(null)
        appDataManager?.setCallId(null)
        connectedCall=null
        isConnected=false
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        if(pubnubSetting!=null)
            pubnubSetting?.removeSetting()
    }
}
