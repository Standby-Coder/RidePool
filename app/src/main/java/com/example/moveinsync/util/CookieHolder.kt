package com.example.moveinsync.util

import java.net.CookieManager
import java.net.CookiePolicy

object CookieHolder {
    private var cookieManager = CookieManager()

    init {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    fun getCookieManager(): CookieManager {
        return cookieManager
    }
}