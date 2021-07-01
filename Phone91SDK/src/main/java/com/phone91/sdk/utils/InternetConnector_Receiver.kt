package com.phone91.sdk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.phone91.sdk.MyApplication
//import com.titan.ui.dashboard.DashboardActivity

class InternetConnector_Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {

            val isVisible = MyApplication.isActivityVisible()

            if (isVisible) {
                val connectivityManager = context
                        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager
                        .activeNetworkInfo

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
