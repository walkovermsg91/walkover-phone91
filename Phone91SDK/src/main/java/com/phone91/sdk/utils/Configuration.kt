package com.phone91.sdk.utils

import com.phone91.sdk.data.AppDataManager
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
import javax.inject.Inject

data class Configuration(val widgetToken: String,
                         val name: String = "",
                         val email: String="",
                         val phone: String = "") {
    @Inject
    lateinit var appDataManager: AppDataManager
    class Builder {
        private lateinit var widgetToken: String
        private var name: String = ""
        private var email: String = ""
        private var phone: String = ""


        fun widgetToken(widgetToken: String) = apply { this.widgetToken = widgetToken }
        fun name(name: String) = apply { this.name = name }
        fun email(email: String) = apply { this.email = email }
        fun phone(phone: String) = apply { this.phone = phone }

        fun build() = Configuration(
            widgetToken,
            name,
            email,
            phone
        )

        fun save(){
//            appDataManager.setWidgetToken(intent.getStringExtra(DashboardActivity.WIDGETTOKEN))
        }
    }
}