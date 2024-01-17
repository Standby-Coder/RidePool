package com.example.moveinsync.network.login

import com.example.moveinsync.model.login.LoginRequest
import com.example.moveinsync.util.CookieHolder.getCookieManager
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {

    @POST("login")
    fun login(
        @Body body: LoginRequest
    ) : Call<ResponseBody>

    companion object {
        operator fun invoke() : LoginAPI {

            val interceptorLogging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .apply{
                    cookieJar(JavaNetCookieJar(getCookieManager()))
                    addInterceptor(interceptorLogging)
                }.build()



            return Retrofit.Builder()
                .baseUrl("192.168.216.93/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(LoginAPI::class.java)
        }
    }
}