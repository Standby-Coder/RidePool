package com.example.moveinsync.model.signup

import androidx.lifecycle.LiveData

interface SignupListener {
    fun onStarted()
    fun onSuccess(response: LiveData<String>)
    fun onFailure(message : String)
}