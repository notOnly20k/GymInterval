package com.cz.gyminterval.base

import androidx.lifecycle.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.slf4j.LoggerFactory

abstract class BaseViewModel: ViewModel(),LifecycleObserver {
    open val logger  = LoggerFactory.getLogger(javaClass.simpleName)

    private var disposable: CompositeDisposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        disposable?.dispose()
        disposable = null

    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun Disposable.bindLifecycle() : Disposable {
        bindToLifecycle(this)
        return this
    }

    fun bindToLifecycle(disposable: Disposable) {
        if (this.disposable == null) {
            this.disposable = CompositeDisposable()
        }

        this.disposable!!.add(disposable)
    }
}