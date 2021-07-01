package com.phone91.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.data.database.AppDBManager
//import co.paystack.android.PaystackSdk
//import com.google.android.gms.ads.MobileAds
//import com.google.android.libraries.places.api.Places
//import com.google.firebase.FirebaseApp
//import com.google.android.libraries.places.api.Places
//import com.phone91.sdk.di.component.DaggerAppComponent
//import com.mbaka.ui.utils.PLACES_API_KEY
//import com.organizers.ondemand.utils.PLACES_API_KEY
//import dagger.android.AndroidInjector
//import dagger.android.DispatchingAndroidInjector
//import dagger.android.HasActivityInjector
//import dagger.android.HasFragmentInjector
//import javax.inject.Inject
//import com.google.android.libraries.places.api.net.PlacesClient
//import com.organizers.ondemand.model.User


/**
 * Created by Ankush on 30/4/18.
 */
open class MyApplication() : Application()/*, HasActivityInjector */{






//    @Inject
//    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
//
////    @Inject
////    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
//
//
//    override fun activityInjector(): AndroidInjector<Activity> {
//        return dispatchingActivityInjector
//    }


//    override fun fragmentInjector(): AndroidInjector<Fragment> {
//        return fragmentInjector
//    }

    override fun onCreate() {
        super.onCreate()

//        context = this
        AppDataManager.getInstance(this)
        // initialize Dagger

//        DaggerAppComponent.builder().application(this).build().inject(this)
//        MobileAds.initialize(this, "ca-app-pub-2650932663979053~8505818395")
//        Places.initialize(getApplicationContext(), PLACES_API_KEY);

//        MobileAds.initialize(this, "ca-app-pub-2650932663979053~8505818395")
//        PaystackSdk.initialize(getApplicationContext());
       ////// MobileAds.initialize(this, " ca-app-pub-2650932663979053~8505818395")
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
//        Places.initialize(getApplicationContext(), PLACES_API_KEY);
//        val placesClient = Places.createClient(this)

    }



    companion object {
        var activityVisible: Boolean = false
        var isChatVisible: Boolean = false
//        var user: User? = null
        private var isInterestingActivityVisible: Boolean = false
//        lateinit var context: Context


        fun isActivityVisible(): Boolean {
            return activityVisible // return true or false
        }

        fun activityResumed() {
            activityVisible = true// this will set true when activity resumed

        }

        fun activityPaused() {
            activityVisible = false// this will set false when activity paused

        }


        @JvmStatic
        fun setisChatVisible(b: Boolean) {
            isChatVisible = b
        }



    }
    fun isInterestingActivityVisible(): Boolean {
        return isInterestingActivityVisible
    }

    fun setInterestingActivityVisible(b: Boolean?) {
        isInterestingActivityVisible = b!!
    }


}