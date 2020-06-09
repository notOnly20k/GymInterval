package com.cz.gyminterval.ui.login

import android.util.Log
import com.cz.gyminterval.base.BaseViewModel
import com.cz.gyminterval.ext.e

class LoginViewModel:BaseViewModel() {

    override fun onStart() {
        super.onStart()
        logger.e { "onstart" }
    }

    override fun onStop() {
        super.onStop()
        logger.e { "onStop" }
    }
}