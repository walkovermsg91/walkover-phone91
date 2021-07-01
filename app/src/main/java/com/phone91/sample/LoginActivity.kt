package com.phone91.sample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.phone91.sdk.mvvm.dashboard.DashboardActivity
//import com.phone91.sample.LoginActivity
//import com.phone91.sdk.mvvm.dashboard.DashboardActivity
//import com.phone91.sdk.mvvm.dashboard.LogoutChain
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLogin()
        btnLogin.setOnClickListener{
            var preferenceUtill= PreferenceUtill(LoginActivity@this)
            if(preferenceUtill.isLogin()){
                preferenceUtill.clear()
                DashboardActivity.logoutChain?.logoutApp()
                finish()
            }else {
                if (edName.text.toString().trim().equals("")) {
                    Toast.makeText(LoginActivity@ this, "Please enter name", Toast.LENGTH_SHORT)
                        .show()

                } else if (edEmail.text.toString().trim().equals("") || !Patterns.EMAIL_ADDRESS.matcher(
                        edEmail.text.toString().trim()
                    ).matches()
                ) {
                    Toast.makeText(
                        LoginActivity@ this,
                        "Please enter valid email",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (edPhone.text.toString().trim().equals("") || !Patterns.PHONE.matcher(
                        edPhone.text.toString().trim()
                    ).matches()
                ) {
                    Toast.makeText(LoginActivity@ this, "Please enter phone", Toast.LENGTH_SHORT)
                        .show()
                } else {




                    DashboardActivity.logoutChain?.logoutApp()
                    preferenceUtill.saveLogin(true)
                    preferenceUtill.saveName(edName.text.toString().trim())
                    preferenceUtill.saveEmail(edEmail.text.toString().trim())
                    preferenceUtill.savePhone(edPhone.text.toString().trim())
                    setLogin()
                }
            }
        }
    }

    private fun setLogin(){
        var preferenceUtill= PreferenceUtill(LoginActivity@this)
        if(preferenceUtill.isLogin()){
            btnLogin.setText("Logout")
            txtName.setText("Hello "+preferenceUtill.getName()+"! Welcome on board")
            txtName.visibility=View.VISIBLE
            edName.visibility=View.GONE
            edEmail.visibility=View.GONE
            edPhone.visibility=View.GONE
        }else {
            btnLogin.setText("Login")
            txtName.visibility = View.GONE
            txtName.visibility=View.GONE
            edName.visibility=View.VISIBLE
            edEmail.visibility=View.VISIBLE
            edPhone.visibility=View.VISIBLE
        }
    }
}