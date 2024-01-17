package com.example.moveinsync.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moveinsync.R
import com.example.moveinsync.databinding.ActivityLoginBinding
import com.example.moveinsync.model.login.LoginListener
import com.example.moveinsync.ui.dashboard.admin.AdminDashboardActivity
import com.example.moveinsync.ui.dashboard.traveller.TravellerDashboardActivity
import com.example.moveinsync.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity(), LoginListener {

    private lateinit var errorTextViewLogin : TextView
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewmodel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.viewmodel = viewmodel
        viewmodel.loginListener = this

        errorTextViewLogin = findViewById(R.id.errorTextViewLogin)
        errorTextViewLogin.visibility = View.INVISIBLE

        signupButton = findViewById(R.id.buttonSignup)
        signupButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun finish(){
        onDestroy()
        finish()
    }

    override fun onStarted() {
        Toast.makeText(this, "Login Started", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(loginResponse: LiveData<String>) {
        loginResponse.observe(this, Observer {
            val values = it.split(",")
            val respCode = values[0]
            val name = values[1]
            val type = values[2]
            if(respCode == "200"){
                Toast.makeText(this, "Login Successful, Welcome $name", Toast.LENGTH_LONG).show()
                lateinit var intent : Intent
                when (type) {
                    "Traveller" -> {
                        intent = Intent(this@LoginActivity, TravellerDashboardActivity::class.java)
                    }
                    "Admin" -> {
                        intent = Intent(this@LoginActivity, AdminDashboardActivity::class.java)
                    }
                }
                startActivity(intent)
                finish()
            }
        })
    }

    override fun onFailure(message: String?) {
        errorTextViewLogin.visibility = View.VISIBLE
        errorTextViewLogin.text = message
    }
}
