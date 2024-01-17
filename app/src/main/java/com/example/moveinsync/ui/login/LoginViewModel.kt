package com.example.moveinsync.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.moveinsync.data.login.UserLogin
import com.example.moveinsync.model.login.LoginListener

class LoginViewModel : ViewModel() {

    var username : String? = null
    var password : String? = null
    var loginListener: LoginListener? = null

    fun onLoginButtonClick(v : View?){
        loginListener?.onStarted()

        if (username.isNullOrEmpty()){
            loginListener?.onFailure("Enter Username")
            return
        }

        if(password.isNullOrEmpty()){
            loginListener?.onFailure("Enter Password")
            return
        }

        val response = UserLogin().userLogin(username!!, password!!)
        loginListener?.onSuccess(response)
    }

}
