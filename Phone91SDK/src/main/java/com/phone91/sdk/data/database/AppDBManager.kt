package com.phone91.sdk.data.database

import android.content.Context
import com.phone91.sdk.model.ChannelObject
import com.phone91.sdk.model.LoginObject
import com.phone91.sdk.model.WidgetDBObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class AppDBManager

constructor(private val context: Context, private var channelDao: ChannelDao, private var loginDao: LoginDao, private var widgetDao: WidgetDao):DBManager{

//    private var channelDao: ChannelDao = SDKDatabase.getInstance(context).channelDao()


    override public fun getChannelByTeamID(teamid: String): Single<ChannelObject> {

        return channelDao.getDetailById(teamid)
    }
    override public fun getNameByChannel(channel: String): Single<ChannelObject> {

        return channelDao.getNAmeByChannel(channel)
    }

    override public fun isFavouriteShow(teamid: String): Single<ChannelObject> {

        return channelDao.getDetailById(teamid)
    }

    override public fun insertChannel(channelObject: ChannelObject): Completable {
        return channelDao.insertChannel(channelObject)
    }

    override fun getLoginDetail(): Single<LoginObject> {
        return  loginDao.getLoginDetail()
    }

    override fun insertLoginDetail(loginObject: LoginObject): Completable {
        return loginDao.insertChannel(loginObject)
    }

    override fun deleteLoginDetail(): Completable  {
       return  loginDao.deleteAllLogin()
    }

    override fun getWidgetDetail(widgetToken: String?): Single<WidgetDBObject> {
        return  widgetDao.getWidgetDetail(widgetToken!!)
    }



    override fun insertWidgetDetail(widgetDBObject: WidgetDBObject): Completable {

        return widgetDao.insertWidget(widgetDBObject)
    }


}