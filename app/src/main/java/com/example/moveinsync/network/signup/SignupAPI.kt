package com.example.moveinsync.network.signup

import com.example.moveinsync.model.signup.SignupRequest
import com.example.moveinsync.util.CookieHolder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupAPI {

    @POST("signup")
    fun login(
        @Body body: SignupRequest
    ) : Call<ResponseBody>

    companion object {
        operator fun invoke() : SignupAPI {

            val interceptorLogging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .apply{
                    cookieJar(JavaNetCookieJar(CookieHolder.getCookieManager()))
                    addInterceptor(interceptorLogging)
                }.build()

            return Retrofit.Builder()
                .baseUrl("192.168.216.93/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(SignupAPI::class.java)
        }
    }
}