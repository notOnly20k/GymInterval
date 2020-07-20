package com.cz.gyminterval.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding


abstract class BaseViewModelActivity<VM : BaseViewModel, VB : ViewDataBinding>:BaseActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = onCreateViewBinding(layoutInflater)
        viewModel = onCreateViewModel()

        lifecycle.addObserver(viewModel)
        binding.lifecycleOwner=this

        setContentView(binding.root)
    }

    abstract fun onCreateViewModel(): VM
    abstract fun onCreateViewBinding(layoutInflater: LayoutInflater): VB
}