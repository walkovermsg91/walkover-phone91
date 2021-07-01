package com.phone91.sdk.module

import android.app.Application
import android.content.Context
import com.phone91.sdk.data.database.*
import com.phone91.sdk.data.preference.AppPreferenceManager
import com.phone91.sdk.data.remote.RemoteDataManager
import com.phone91.sdk.utils.Constants


//@Module(includes = [ActivityViewModelModule::class])
class MyApplicationModule {


    private   var  appPreferenceManager: AppPreferenceManager?=null
    private   var  appDBManager: AppDBManager?=null
    private   var  remoteDataManager: RemoteDataManager?=null
    private   var  sdkDatabase: SDKDatabase?=null

    companion object {
        private var instance: MyApplicationModule?=null
        private  lateinit var  mApp: Application
        private  lateinit var  mContext: Context
        fun getInstance( context: Application): MyApplicationModule? {
            if(instance==null) {
                mApp=context
                mContext=mApp.applicationContext
                instance = MyApplicationModule()
            }
            return instance
        }
    }

    public fun getPreferenceSource():AppPreferenceManager?{
            if(appPreferenceManager==null)
                appPreferenceManager= AppPreferenceManager(mContext,Constants.PREF_NAME)
        return appPreferenceManager
    }

    public fun provideAppDBManager():DBManager?{
        if(appDBManager==null) {
            provideSDKDatabase()
            appDBManager = AppDBManager(mContext, provideChannelDao()!!,provideLoginDao()!!, provideWidgetDao()!!)
        }
        return appDBManager
    }

    public fun provideRemoteDataManager():RemoteDataManager?{
        if(remoteDataManager==null) {
            remoteDataManager = RemoteDataManager(getPreferenceSource()!!)
        }
        return remoteDataManager
    }

    private fun provideSDKDatabase():SDKDatabase?{
        if(sdkDatabase==null) {
            sdkDatabase =  SDKDatabase.getInstance(mApp)/*Room.databaseBuilder(
                mApp,
                SDKDatabase::class.java, "phone91sdk.db").build()*/
        }
        return sdkDatabase
    }


    private fun provideChannelDao():ChannelDao?{
        return sdkDatabase?.channelDao()
    }

    private fun provideLoginDao():LoginDao?{
        return sdkDatabase?.loginDao()
    }

    private fun provideWidgetDao():WidgetDao?{
        return sdkDatabase?.widgetDao()
    }


}