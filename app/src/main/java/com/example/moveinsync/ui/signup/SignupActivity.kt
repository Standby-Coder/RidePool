package com.example.moveinsync.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moveinsync.R
import com.example.moveinsync.databinding.ActivitySignupBinding
import com.example.moveinsync.model.signup.SignupListener
import com.example.moveinsync.ui.login.LoginActivity

class SignupActivity : AppCompatActivity(), SignupListener {

    private lateinit var errorTextViewSignup : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        val viewmodel = ViewModelProvider(this)[SignupViewModel::class.java]

        binding.viewmodel = viewmodel
        viewmodel.signupListener = this

        errorTextViewSignup = findViewById(R.id.errorTextViewSignup)
        errorTextViewSignup.visibility = View.INVISIBLE
    }

    override fun finish(){
        onDestroy()
        finish()
    }

    override fun onStarted() { }

    override fun onSuccess(response: LiveData<String>) {
        response.observe(this, Observer {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    override fun onFailure(message: String) {
        errorTextViewSignup.text = message
        errorTextViewSignup.visibility = View.VISIBLE
    }

}