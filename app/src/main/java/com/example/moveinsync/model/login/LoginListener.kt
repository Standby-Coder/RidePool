package com.example.moveinsync.model.login

import androidx.lifecycle.LiveData

interface LoginListener {
    fun onStarted()
    fun onSuccess(loginResponse: LiveData<String>)
    fun onFailure(message : String?)
}