package com.phone91.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    private var widgetToken= "28b43"
    private var widgetToken= "28b15"
//    private var widgetToken= "88461"

//    Set below values, if you do not ask user for these detail
//    private var name= "write_name"
//    private var email= "aob@vb.com"
//    private var phone= "999999987"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTest.setOnClickListener{
            var intent= Intent(this, DashboardActivity::class.java)
            intent.putExtra(DashboardActivity.WIDGETTOKEN,widgetToken)

    //Uncomment below code, if you want to pass login detail of user
           var preferenceUtill= PreferenceUtill(MainActivity@this)
            if(preferenceUtill.isLogin()) {
//                Log.d("NAME: ",preferenceUtill.getName())
//                Log.d("EMAIL: ",preferenceUtill.getEmail())
//                Log.d("PHONE: ",preferenceUtill.getPhone())
                intent.putExtra(DashboardActivity.NAME, preferenceUtill.getName())
                intent.putExtra(DashboardActivity.EMAIL, preferenceUtill.getEmail())
                intent.putExtra(DashboardActivity.PHONE, preferenceUtill.getPhone())
            }

            startActivity(intent)
        }
        btnSetting.setOnClickListener{
            startActivity(Intent(MainActivity@this, LoginActivity::class.java))
        }
    }
}
