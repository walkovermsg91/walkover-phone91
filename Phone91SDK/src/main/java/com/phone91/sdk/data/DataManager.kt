package com.phone91.sdk.data

import com.phone91.sdk.data.database.DBManager
import com.phone91.sdk.data.preference.PreferenceSource
import com.phone91.sdk.data.remote.RemoteSource


interface DataManager :  RemoteSource,PreferenceSource,DBManager {

    enum class LoggedInMode(val type: Int) {
        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }

    enum class LoggedInType(val type: Int) {
        LOGGING_IN_FIRST_TIME(0),
        LOGGING_IN_REGULAR(1)
    }
}
