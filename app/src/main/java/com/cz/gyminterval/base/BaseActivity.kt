package com.cz.gyminterval.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.slf4j.LoggerFactory

abstract class BaseActivity:AppCompatActivity() {
    protected val logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }
    private var disposables: CompositeDisposable? = null

    override fun onStop() {
        super.onStop()

        disposables?.dispose()
        disposables = null
    }

    fun Disposable.bindToLifecycle(): Disposable {
        if (disposables == null) {
            disposables = CompositeDisposable()
        }

        disposables!!.add(this)
        return this
    }


}