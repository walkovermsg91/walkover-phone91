package com.phone91.sample

import android.content.Context

class PreferenceUtill(val context: Context) {
    val sharedPref =context.getSharedPreferences("perf_phone91_sdk", Context.MODE_PRIVATE)

    public fun saveName( name: String){
        sharedPref.edit().putString("name",name).commit()
    }
    public fun saveEmail( email: String){
        sharedPref.edit().putString("email",email).commit()
    }
    public fun savePhone( phone: String){
        sharedPref.edit().putString("phone",phone).commit()
    }
    public fun saveLogin( login: Boolean){
        sharedPref.edit().putBoolean("isLogin",login).commit()
    }

    public fun isLogin():Boolean{
        return sharedPref.getBoolean("isLogin",false)
    }

    public fun getEmail():String?{
        return sharedPref.getString("email",null)
    }

    public fun getPhone():String?{
        return sharedPref.getString("phone",null)
    }

    public fun getName():String?{
        return sharedPref.getString("name",null)
    }

    public fun clear(){
        sharedPref.edit().clear().commit()
    }

}