package com.example.moveinsync.ui.signup

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.moveinsync.data.signup.UserSignup
import com.example.moveinsync.model.signup.SignupListener

class SignupViewModel : ViewModel() {
    var signupListener : SignupListener? = null

    var name : String? = null
    var phone : String? = null
    var email : String? = null
    var password : String? = null

    fun onSignupButtonClick(v : View?){
        signupListener?.onStarted()

        if(name.isNullOrEmpty()){
            signupListener?.onFailure("Enter Name")
            return
        }

        if(phone.isNullOrEmpty()){
            signupListener?.onFailure("Enter Phone")
            return
        }

        if(email.isNullOrEmpty()){
            signupListener?.onFailure("Enter Email")
            return
        }

        if(password.isNullOrEmpty()){
            signupListener?.onFailure("Enter Password")
            return
        }

        val response = UserSignup().userSignup(name!!, phone!!, email!!, password!!)
        signupListener?.onSuccess(response)
    }
}