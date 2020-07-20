package com.cz.gyminterval.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cz.gyminterval.base.BaseViewModelActivity
import com.cz.gyminterval.databinding.ActivityLoginBinding
import com.cz.gyminterval.ext.appComponent
import com.cz.gyminterval.ext.e

class LoginActivity:BaseViewModelActivity<LoginViewModel,ActivityLoginBinding>(), LoginViewModel.Navgation {
    override fun showWaitDialog(isShow: Boolean) {

    }

    override fun loginSuccess(userId: String) {
        Toast.makeText(this,userId,Toast.LENGTH_SHORT).show()
    }

    override fun onCreateViewModel(): LoginViewModel {
        return ViewModelProvider.AndroidViewModelFactory(application).create(LoginViewModel::class.java)

    }

    override fun onCreateViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
       return  ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel=viewModel
    }
}