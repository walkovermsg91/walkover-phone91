package com.phone91.sdk.routing

import android.content.Context
import com.phone91.sdk.routing.Navigator
//import com.phone91.sdk.routing.createpost.CreatePostController
//import com.mbaka.ui.routing.createpost.CreatePostRouteManager
import com.phone91.sdk.routing.dashboard.DashboardController
import com.phone91.sdk.routing.dashboard.DashboardRouteManager

import javax.inject.Inject

class NavigationController
//@Inject
private constructor
(val context: Context,private var dashboardRouteManager: DashboardRouteManager) : Navigator,DashboardController by dashboardRouteManager {


    companion object {
      private var instance:NavigationController?=null
         fun  getInstance( context: Context,  dashboardRouteManager: DashboardRouteManager):NavigationController{
             if(instance==null)
                   instance=NavigationController(context,dashboardRouteManager)
            return instance!!
        }
    }
}