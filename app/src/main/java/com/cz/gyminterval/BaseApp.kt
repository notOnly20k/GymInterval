package com.cz.gyminterval

import android.app.Application
import org.slf4j.LoggerFactory

class BaseApp : Application(), AppComponent{

    override fun onCreate() {
        super.onCreate()
        instance=this
    }
    companion object {
        private val logger = LoggerFactory.getLogger("BaseApp")

        @JvmStatic
        lateinit var instance: BaseApp
            private set
    }

}