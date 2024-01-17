package com.example.moveinsync.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moveinsync.R
import com.example.moveinsync.ui.login.LoginActivity
import com.example.moveinsync.util.LocationUpdateService
import com.example.moveinsync.util.NetworkUtils

class MainActivity : AppCompatActivity() {

    private lateinit var errorText: TextView
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isConnected = NetworkUtils.isConnectedToInternet(applicationContext)
        val serviceIntent = Intent(this@MainActivity, LocationUpdateService::class.java)
        startService(serviceIntent)
        errorText = findViewById(R.id.errorTextView)
        loginButton = findViewById(R.id.buttonLogin)
        loginButton.visibility = View.INVISIBLE

        if (isConnected){
            errorText.text = ""
            loginButton.visibility = View.VISIBLE

            loginButton.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        } else{
            errorText.text = R.string.errorText.toString()
        }
    }

    override fun finish() {
        super.finish()
        onDestroy()
    }
}