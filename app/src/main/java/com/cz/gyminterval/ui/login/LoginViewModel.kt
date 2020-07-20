package com.cz.gyminterval.ui.login

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.cz.gyminterval.AppComponent
import com.cz.gyminterval.base.BaseViewModel
import com.cz.gyminterval.ext.e

class LoginViewModel(val appComponent: AppComponent, val navgation: LoginViewModel.Navgation):BaseViewModel() {
//    val name = ObservableField<String>(appComponent.preference.lastUser?.name)
//    val password = ObservableField<String>(appComponent.preference.lastUser?.password)
    val nameError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    private val isLogging = ObservableBoolean(false)

    override fun onStart() {
        super.onStart()
        logger.e { "onstart" }

    }

    override fun onStop() {
        super.onStop()
        logger.e { "onStop" }
    }


    fun login(){
        navgation.loginSuccess("ssss")
    }

    interface Navgation {
        fun loginSuccess(userId: String)
        fun showWaitDialog(isShow: Boolean)
    }
}