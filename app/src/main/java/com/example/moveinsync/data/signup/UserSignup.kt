package com.example.moveinsync.data.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moveinsync.model.signup.SignupRequest
import com.example.moveinsync.network.signup.SignupAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSignup {
    fun userSignup(name: String, phone: String, email: String ,password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()
        val request = SignupRequest(name, phone, email, password)

        SignupAPI().login(request)
            ?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val repCode = response.body()?.string()
                        if (repCode != null) {
                            when (repCode) {
                                "200" -> {
                                    loginResponse.value = "Signup Successful!"
                                }
                                "404" -> {
                                    loginResponse.value = "Email/Phone already in use"
                                }
                                "403" -> {
                                    loginResponse.value = "Unauthorized Use"
                                }
                                "400" -> {
                                    loginResponse.value = "Missing Fields"
                                }
                            }
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