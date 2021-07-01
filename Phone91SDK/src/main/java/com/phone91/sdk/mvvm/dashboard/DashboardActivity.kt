package com.phone91.sdk.mvvm.dashboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.gson.Gson
import com.phone91.sdk.R
import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.model.*
import com.phone91.sdk.mvvm.base.BaseActivity
import com.phone91.sdk.mvvm.dashboard.home.HomeFragment
import com.phone91.sdk.mvvm.dashboard.teams.TeamFragment
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class DashboardActivity : BaseActivity<DashboardActivityVM>(), LogoutChain
  /*  ,NavigationView.OnNavigationItemSelectedListener*/ {




//    @Inject
     var dashboardActivityVM: DashboardActivityVM?=null



//    @Inject
//    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var k = 0
    public var pos = -1
    private var widgetObject:WidgetObject?=null
    private var paramListObject:ParamListObject?=null
    private var clientObject:ClientObject?=null
    public var channelListObject: ChannelListObject?=null

    val RETURN_BACK: Int = 1;
    private lateinit var progressDialog: Dialog
    public  var name: String?=null
    public  var email: String?=null
    public  var phone: String?=null
    public  var isCallInitiated: Boolean=false

    override fun getViewModel(): DashboardActivityVM {
        if(dashboardActivityVM==null)
            dashboardActivityVM= DashboardActivityVM(appDataManager!!)
       return dashboardActivityVM!!
    }
//    @Inject
     var appDataManager: AppDataManager?=null



    companion object {
        var WIDGETTOKEN="WidgetToken"
        var NAME="Name"
        var EMAIL="Email"
        var PHONE="Phone"
        var UUIDS="Uuid"

        fun startInstanceWithBackStackCleared(context: Context?) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

       var logoutChain:LogoutChain?=null
       var isCallEnable:Boolean=false


    }

//    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
//        return fragmentDispatchingAndroidInjector
//    }


    //    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        appDataManager= AppDataManager.getInstance(application)
        super.onCreate(savedInstanceState)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_dashboard)
        logoutChain=this

        if(intent.getStringExtra(WIDGETTOKEN)==null || intent.getStringExtra(WIDGETTOKEN).trim().equals("")){
            showShortToast("Please add valid WidgetToken. ")
            finish()
        }
        initializeProgressLoader()
        observeError()
        observeLoading()
        observeSuccess()
        appDataManager?.setWidgetToken(intent.getStringExtra(WIDGETTOKEN))
        if(intent.hasExtra(NAME)|| intent.hasExtra(EMAIL) || intent.hasExtra(PHONE)) {
            if (intent.getStringExtra(NAME) == null || intent.getStringExtra(NAME).trim().equals("")) {
                showShortToast("Please add name.")
                finish()
            }
            if (intent.getStringExtra(EMAIL)  == null || intent.getStringExtra(EMAIL).trim().equals("")) {
                showShortToast("Please add email.")
                finish()
            }
            if (intent.getStringExtra(PHONE) == null || intent.getStringExtra(PHONE).trim().equals("")) {
                showShortToast("Please add phone.")
                finish()
            }

            name=intent.getStringExtra(NAME)
            email=intent.getStringExtra(EMAIL)
            phone=intent.getStringExtra(PHONE)

        }
            dashboardActivityVM?.getLoginDetailFromDB()

        jistiSetting()

    }

    public fun callOnLoad(){
//        appDataManager.setUUID( UUID.randomUUID().toString())
        dashboardActivityVM?.getClientDetail()
        dashboardActivityVM?.getClientParam()
    }


    public fun getPubnubDetail() {
        dashboardActivityVM?.getPubnubDetail()
    }

    public fun getChannelList() {
        dashboardActivityVM?.getChannelList(email,appDataManager?.getUniqId())
    }

    public fun getChannelList1(name: String, email: String, phone: String) {
        this.name=name
        this.email=email
        this.phone=phone
        dashboardActivityVM?.getChannelList1(email, name, phone,appDataManager?.getUniqId())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }


    public fun checkChannelDetail(team_id: String?){
        if(channelLits!=null) {
//            appDataManager.setUUID(channelListObject?.uuid!!);
//            var homeFragment= HomeFragment.newInstance(widgetObject!!)
//            navigationController.openHomeFragment(this, R.id.fl_dashboard_container, homeFragment)

            var chnl:Channel?=null
            if(channelLits?.any {
                    chnl=it
                    it.team_id==team_id

                }!!){
                appDataManager?.setChannel(chnl?.channel!!);
                 Log.d("checkChannelDetail","isCallEnable :"+chnl?.showCallBtn)
                if(chnl!=null)
                  isCallEnable=chnl!!.showCallBtn
//                chnl?.showCallBtn=true
                chnl?.uuid=channelListObject?.uuid
                dashboardActivityVM?.getChannelDetailHistory(chnl)

            }else {
//                appDataManager.setUUID( UUID.randomUUID().toString())


                val jsonParams: HashMap<String?, Any?> = HashMap()
                if (name != null){
                    jsonParams.put("name",name)
                    jsonParams.put("param_1",name)
                }
                if (phone != null) {
                    jsonParams.put("number", phone)
                    jsonParams.put("param_2", phone)
                }
                if (email != null) {
                    jsonParams.put("mail", email)
                    jsonParams.put("param_3", email)
                }
                dashboardActivityVM?.getChannelDetail(
                    null,
                    jsonParams,
                    appDataManager?.getUUID(),
                    team_id
                )
            }
        }/*else if(team_id!=null){
            appDataManager.setUUID( UUID.randomUUID().toString())
            dashboardActivityVM.getChannelDetail(
                null,
                name,
                phone,
                email,
                appDataManager.getUUID(),
//                UUID.randomUUID().toString(),
                team_id
            )
        }*/else {
            val jsonParams: HashMap<String?, Any?> = HashMap()
            if (name != null){
                jsonParams.put("name",name)
                jsonParams.put("param_1",name)
            }
            if (phone != null) {
                jsonParams.put("number", phone)
                jsonParams.put("param_2", phone)
            }
            if (email != null) {
                jsonParams.put("mail", email)
                jsonParams.put("param_3", email)
            }
//            appDataManager.setUUID(UUID.randomUUID().toString())
            dashboardActivityVM?.getChannelDetail(
                null,
               jsonParams,
                appDataManager?.getUUID(),
//                UUID.randomUUID().toString(),
                team_id
            )
        }
    }





    private fun observeError() {
        dashboardActivityVM?.error?.observeForever {
            showErrorToast(it!!)
        }
    }


    private fun observeLoading() {

        dashboardActivityVM?.loading?.observeForever {
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
        dashboardActivityVM?.widgetInfo?.observeForever {
            if (it!=null) {
                 widgetObject = Gson().fromJson(it, WidgetObject::class.java)
                Log.d("Hello1", widgetObject.toString())




//                if(appDataManager.getWidgetToken())
                    dashboardActivityVM?.getWidgetDetailFromDB(appDataManager?.getWidgetToken())
//                getChannelList()
            }

        }

        dashboardActivityVM?.paramListInfo?.observeForever {
            if (it!=null) {

                Log.d("Hello4", it.toString())
                 paramListObject = it
                if(paramListObject!=null ) {

                    Log.d("Hello", "paramListObject not null")
                    if(paramListObject?.default_params!=null){
                        Log.d("Hello", "default_params not null")
                        if(paramListObject?.default_params?.size!!>0){
                            Log.d("Hello", "Team size is "+paramListObject?.default_params?.size.toString())
                        }
                    }
                    Log.d("Hello2", "Team size is "+paramListObject?.toString())

                }
            }

        }
 dashboardActivityVM?.clientInfo?.observeForever {
            if (it!=null) {
                clientObject = it
                dashboardActivityVM?.insertLoginDetail(LoginObject(1,name,phone,email))
                dashboardActivityVM?.getChannelList1(email,name,phone,appDataManager?.getUUID())
            }

        }
        dashboardActivityVM?.finishError?.observeForever {
            if (it) {
                showErrorToast("No Internet")
               finish()
            }

        }


        dashboardActivityVM?.channelInfo?.observeForever {channelObject->
            if (channelObject!=null) {
                if(channelObject.channel!=null && channelObject.channel?.trim()?.length!! >0){
                    if(channelObject.team_id==null)
                        channelObject.team_id="default_team"
//                    dashboardActivityVM.insertOnDB(channelObject)

                    appDataManager?.setChatId(channelObject.id.toString());
                    appDataManager?.setChannel(channelObject.channel!!);
                    appDataManager?.setUUID(channelObject.uuid!!);

                    if(channelObject?.uuid!=null){
                        dashboardActivityVM?.insertWidgetDetail(WidgetDBObject(appDataManager?.getWidgetToken()!!,appDataManager?.getUniqId(), appDataManager?.getUUID()))
                    }
                    Log.d("channelInfo","isCallEnable :"+channelObject.call_enabled)
                    isCallEnable=channelObject.call_enabled

                    this.name=channelObject.name
                    this.email=channelObject.mail
                    this.phone=channelObject.number
//                    appDataManager.setUniqId(channelObject.unique_id!!);

                        getPubnubDetail()


//                    navigationController.openTeamFragment(this, R.id.fl_dashboard_container, TeamFragment.newInstance(widgetObject))
                }else{
                    showErrorToast(getString(R.string.error_message_invalid))
                }

            }

        }

        dashboardActivityVM?.pubnubInfo?.observeForever {
            if (it!=null) {
                //                    {"pubkey":"pub-c-7efeae95-f505-4c40-99c4-04015a415abe",
//                        "subkey":"sub-c-41ea6378-7d3f-11e9-945c-2ea711aa6b65","authkey":"a6cfa71a1c774a5ab08e168fe17a0127"}
                var pubNubObject = Gson().fromJson(it, PubNubObject::class.java)
                if(pubNubObject.authkey!=null){
                    appDataManager?.setPubkey(pubNubObject.pubkey!!);
                    appDataManager?.setSubkey(pubNubObject.subkey!!);
                    appDataManager?.setAuth(pubNubObject.authkey!!);
                    activateChat();
//                    PubnubSetting.initPubNub(appDataManager)
//                    showChat()
                }else{
                    showErrorToast(getString(R.string.error_message_auth))
                }
            }

        }


//        dashboardActivityVM.channelObjectData.observeForever {
//            if (it!=null) {
//                if(it.channel==null) {
////                    dashboardActivityVM.getChannelDetail(
////                        null,
////                        name,
////                        phone,
////                        email,
////                        appDataManager.getUUID(),
//////                        UUID.randomUUID().toString(),
////                        it.team_id
////                    )
//                }else{
////                    appDataManager.setChannel(it.channel!!);
////                    appDataManager.setUUID(it.uuid!!);
//                    var homeFragment= HomeFragment.newInstance(widgetObject!!)
//                    navigationController.openHomeFragment(this, R.id.fl_dashboard_container, homeFragment)
//
//                }
//            }
//
//        }



        dashboardActivityVM?.loginObjectData?.observeForever {
            if (it!=null) {
                if(it.name==null) {
                    Log.d("name is null","yes")
                    callOnLoad()
                }else{
                    Log.d("name is null",it.name)
                    name=it.name
                    email=it.mail
                    phone=it.number
                    callOnLoad()
                }
            }

        }


        dashboardActivityVM?.widgetObjectData?.observeForever {
            if (it!=null) {
                if(it.widgetToken==null ||  it.widgetToken.equals("")) {
                    appDataManager?.setUUID(null)
                    getChannelList()
                }else{
//                    name=it.name
//                    email=it.mail
//                    phone=it.number
                    appDataManager?.setUUID(it.uuid!!)
                    getChannelList()
                }
            }else {
                appDataManager?.setUUID(null)
                getChannelList()
            }

        }

        dashboardActivityVM?.channelListError?.observeForever{
            if(it!=null && it.equals("NOT FOUND")){
                getSubscriptionList()

            }
        }

        dashboardActivityVM?.channelListData?.observeForever{
            if(it!=null){
                  channelListObject=it


                appDataManager?.setUUID(channelListObject?.uuid!!)




                if(channelListObject?.unique_id!=null)
                  appDataManager?.setUniqId(channelListObject?.unique_id!!)

                if(channelListObject?.uuid!=null){
                    dashboardActivityVM?.insertWidgetDetail(WidgetDBObject(appDataManager?.getWidgetToken()!!,appDataManager?.getUniqId(), appDataManager?.getUUID()))
                }
//                if(channelListObject?.name!=null) {
//                    name = channelListObject?.name
//                    email = channelListObject?.mail
//                    phone = channelListObject?.number
//                }
//                if(channelListObject?.channels!=null && channelListObject?.channels?.size!!>0) {
//
//                    setClient(channelListObject?.channels?.get(0)?.channel,name!!, email!!, phone!!,channelListObject?.uuid)
//                }

                getSubscriptionList()
            }
        }

        dashboardActivityVM?.channelListData1?.observeForever{
            if(it!=null){
                channelListObject=it

                if(channelListObject?.unique_id!=null)
                appDataManager?.setUniqId(channelListObject?.unique_id!!)

                appDataManager?.setUUID(channelListObject?.uuid!!)
                if(channelListObject?.uuid!=null){
                    dashboardActivityVM?.insertWidgetDetail(WidgetDBObject(appDataManager?.getWidgetToken()!!,appDataManager?.getUniqId(), appDataManager?.getUUID()))
                }

//                if(channelListObject?.channels!=null && channelListObject?.channels?.size!!>0 &&  channelListObject?.channels?.size==1) {
//
//                    setClient(channelListObject?.channels?.get(0)?.channel,name!!, email!!, phone!!,channelListObject?.uuid)
//                }


                if(channelListObject!=null && channelListObject?.channels != null && channelListObject?.channels?.size!! > 0) {


                    if(channelListObject?.name!=null) {
                        name = channelListObject?.name
                        email = channelListObject?.mail
                        phone = channelListObject?.number
                    }
                    if (widgetObject?.teams != null && widgetObject?.teams?.size!! > 0 ) {
                        channelLits = channelListObject?.channels?.filter { listObject ->
                            widgetObject?.teams?.any { listObject?.team_id?.equals(it.id)!!}!!
                        } as ArrayList<Channel>

                    } else {
                        channelLits=channelListObject?.channels
                        checkChannelDetailFromChat()
                    }
//            subscribeChannel(channelLits)

                }else
                    createChannel()
            }else{
                createChannel()
            }

        }
    }
    var channelLits: ArrayList<Channel>?= null
    public fun getSubscriptionList(){
        Log.d("channelListObject",channelListObject.toString())
        if(channelListObject!=null && channelListObject?.channels != null && channelListObject?.channels?.size!! > 0) {

            if (widgetObject?.teams != null && widgetObject?.teams?.size!! > 0 ) {
                 channelLits = channelListObject?.channels?.filter { listObject ->

                    widgetObject?.teams?.any { listObject?.team_id?.equals(it.id)!! }!!
                } as ArrayList<Channel>



            } else {
                channelLits=channelListObject?.channels
            }
//            subscribeChannel(channelLits)

        }
//        afterPubNubDetail()
        showChatWindow()
    }

    fun showChat() {
        var homeFragment = HomeFragment.newInstance(widgetObject!!)
        if(widgetObject?.teams!=null && widgetObject?.teams?.size!! >0) {
//                     channelObject . team_id ="default_team"

                    navigationController.openHomeFragmentR(
                        this,
                        R.id.fl_dashboard_container,
                        homeFragment)
                    }else
                        navigationController.openHomeFragment(
                            this,
                            R.id.fl_dashboard_container,
                            homeFragment)
                }

    fun showChatWindow() {

        if(widgetObject?.teams!=null && widgetObject?.teams?.size!! >0) {
            Log.d("widgetObject?.teams","widgetObject?.teams")
            navigationController.openTeamFragment(this, R.id.fl_dashboard_container, TeamFragment.newInstance(widgetObject!!))
        }else {
            Log.d("homeFragment","homeFragment")
            homeFragment = HomeFragment.newInstance(widgetObject!!)
            navigationController.openHomeFragment(
                this,
                R.id.fl_dashboard_container,
                homeFragment!!
            )
        }
    }

    public fun subscribeChannel(channelLits: ArrayList<Channel>?) {

    }

    public fun afterPubNubDetail() {

        if(widgetObject?.teams!=null && widgetObject?.teams?.size!! >0){
            navigationController.openTeamFragment(this, R.id.fl_dashboard_container, TeamFragment.newInstance(widgetObject!!))
        }else{
            checkChannelDetail(null)
//           if(channelLits!=null) {
//               var homeFragment= HomeFragment.newInstance(widgetObject!!)
//               navigationController.openHomeFragment(this, R.id.fl_dashboard_container, homeFragment)
//           }else
//            dashboardActivityVM.getChannelDetail(
//                null,
//                name,
//                phone,
//                email,
//                appDataManager.getUniqId()!!,
//                null
//            )
        }
    }

    public fun logout(){
        finish()
    }

    private fun initializeProgressLoader() {
        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setContentView(R.layout.dialog_progress)
    }


    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount <= 0) {
//            ++k
//            when (k) {
//                1 -> {
//                    Toast.makeText(this, getString(R.string.back_press_message), Toast.LENGTH_SHORT).show()
//                    Handler().postDelayed({ k = 0 }, 2000)
//                }
//                2 -> super.onBackPressed()
//                else -> super.onBackPressed()
//            }
//        } else {
//
//            super.onBackPressed()
//        }

    }




    override fun onPause() {

        super.onPause()
        Log.d("onPauseDashboard", "onPauseDashboard")
//        MyApplication.activityPaused()// On Pause notify the Application

    }

    override fun onResume() {
        super.onResume()
//        checkPermission()
        Log.d("onResumeDashboard", "onResumeDashboard")
//        MyApplication.activityResumed()// On Resume notify the Application
//        setImageAndName()
        if(isCallInitiated) {
            isCallInitiated=false
            homeFragment?.callDone()
        }

    }


    public fun removeAllFragments() {
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }




    fun removeFragment() {
//        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()

    }


    fun removeFragment1() {
//        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.popBackStack()

    }


    fun removeFragmentFromS(category: String?, subcategory: String?, zipcode: String?) {
//        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
//        supportFragmentManager.popBackStack()
//        showChooseUserFragmentS(category, subcategory, zipcode)

    }



   public fun close(){
       finish()
   }







    override fun onStop() {
        super.onStop()
    }


     public fun setClient(channel:String?,name: String, email:String, phone:String,uuid:String?){
         this.name=name
         this.phone=phone
         this.email=email

         dashboardActivityVM?.
             setClient(channel,name, email, phone, uuid)

     }

    public fun getClientParam(){
        dashboardActivityVM?.getClientParam()

    }
//    client-param
    var homeFragment:HomeFragment?=null
  public fun callTeamChat(){
      isCallEnable=false
     homeFragment = HomeFragment.newInstance(widgetObject!!)
    navigationController.openHomeFragmentR(
        this,
        R.id.fl_dashboard_container,
        homeFragment!!)
}




    public fun checkChannelDetailFromChat(){
        if(channelLits!=null) {

            var chnl:Channel?=null

            if(channelLits?.size!!>0 && (widgetObject?.teams==null || widgetObject?.teams?.size==0)){
                chnl=channelLits?.get(0)
                appDataManager?.setChatId(chnl?.id.toString());
                appDataManager?.setChannel(chnl?.channel!!);
                homeFragment?.isFirst=false
                Log.d("chkChanelDetailFromChat", "isCallEnable :" + chnl?.showCallBtn)
                if (chnl != null)
                    isCallEnable = chnl!!.showCallBtn
//                chnl?.showCallBtn=true
                chnl?.uuid = channelListObject?.uuid
                dashboardActivityVM?.getChannelDetailHistory(chnl)
            }
            else if(channelLits?.any {
                    chnl=it
//                    it.team_id==widgetObject?.teams?.get(pos)?.id

                    it.team_id?.equals(widgetObject?.teams?.get(pos)?.id)!!

                }!!){
                appDataManager?.setChatId(chnl?.id.toString());
                appDataManager?.setChannel(chnl?.channel!!);
                Log.d("chkChanelDetailFromChat","isCallEnable :"+chnl?.showCallBtn)
                if(chnl!=null)
                    isCallEnable=chnl!!.showCallBtn
//                chnl?.showCallBtn=true
                chnl?.uuid=channelListObject?.uuid
                dashboardActivityVM?.getChannelDetailHistory(chnl)

            }
//            else
//                dashboardActivityVM.getChannelDetail(
//                    null,
//                    name,
//                    phone,
//                    email,
//                    appDataManager.getUUID(),
//                    widgetObject?.teams?.get(pos)?.id
//                )
        }
    }


    public fun createChannel(){
     if(pos==-1) {
         val jsonParams: HashMap<String?, Any?> = HashMap()

         for( parmObject in paramListObject?.default_params!!){
             if(parmObject.name.equals("name") && name != null)
                 jsonParams.put(parmObject.id, name)
             else if(parmObject.name.equals("number") && phone != null)
                 jsonParams.put(parmObject.id, phone)
             else if(parmObject.name.equals("mail") && email != null)
                 jsonParams.put(parmObject.id, email)
         }

         if (name != null) {
             jsonParams.put("name", name)
         }
         if (phone != null) {
             jsonParams.put("number", phone)
         }
         if (email != null) {
             jsonParams.put("mail", email)
         }
         dashboardActivityVM?.getChannelDetail(
             null,
             jsonParams,
             appDataManager?.getUUID(),
             null
         )
     }else {
         val jsonParams: HashMap<String?, Any?> = HashMap()
         for( parmObject in paramListObject?.default_params!!){

             if(parmObject.name.equals("name") && name != null)
                 jsonParams.put(parmObject.id, name)
             else if(parmObject.name.equals("number") && phone != null)
                 jsonParams.put(parmObject.id, phone)
             else if(parmObject.name.equals("mail") && email != null)
                 jsonParams.put(parmObject.id, email)
         }
         if (name != null){
             jsonParams.put("name",name)
         }
         if (phone != null) {
             jsonParams.put("number", phone)
         }
         if (email != null) {
             jsonParams.put("mail", email)
         }
         dashboardActivityVM?.getChannelDetail(
             null,
             jsonParams,
             appDataManager?.getUUID(),
             widgetObject?.teams?.get(pos)?.id.toString()
         )
     }
        }

  private fun activateChat(){
      homeFragment?.callPubnubSetting();
  }

    override public fun logoutApp(){
        if(appDataManager!=null){
            appDataManager?.logoutFromPreffrence()
            dashboardActivityVM?.deleteLoginDetail()
        }
    }


    private fun jistiSetting(){
        val serverURL: URL
        serverURL = try {
            URL("https://marlin.phone91.com") // for production
//            URL("https://sailfish.phone91.com") // for test
//            URL("https://sip.phone91.com:4444") // nor working

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)

            .setWelcomePageEnabled(false)

            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }

    public fun initiateCall( roomId:String,isAudioOnly: Boolean=true, token:String){
        val options = JitsiMeetConferenceOptions.Builder()

       if(isAudioOnly) {
           options.setVideoMuted(true)
           options.setAudioOnly(true)
       }
        val set= options.setRoom(roomId)
            .setToken(token)


            .build()



        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(this, set)
        isCallInitiated=true


    }


    public fun JitsiMeetActivity.show( context: Context,  options:JitsiMeetConferenceOptions): Unit {

        val intent = Intent(context, JitsiMeetActivity::class.java)
        intent.action = "org.jitsi.meet.CONFERENCE"
        intent.putExtra("JitsiMeetConferenceOptions", options)
        context.startActivity(intent)
    }



}
