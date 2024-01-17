package com.example.moveinsync.data.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moveinsync.model.login.LoginRequest
import com.example.moveinsync.network.login.LoginAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLogin {

    fun userLogin(username: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()
        val request = LoginRequest(username, password)

        LoginAPI().login(request)
            ?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val rep = response.body()?.string()
                        val values = rep?.split(",")
                        val repCode = values?.get(0)
                        val name = values?.get(1)
                        if (rep != null) {
                            if(repCode.equals("200")) {
                                loginResponse.value = "Login Successful!\nWelcome $name"
                            }
                            else
                                loginResponse.value = "Wrong Username/Password"
                        }
                    } else {
                        loginResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    loginResponse.value = t.message
                }
            })

        return loginResponse
    }

}
