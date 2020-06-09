package com.cz.gyminterval.ext

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("RxJava")

fun <T> Single<T>.doOnLoadingState(action1: (Boolean) -> Unit): Single<T> {
    return doOnSubscribe { action1(true) }
            .doOnEvent { _, _ -> action1(false) }
}

fun <T> Observable<T>.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Observable<T> {
    return onErrorResumeNext{ throwable : Throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Observable.empty<T>()
    }
}

fun <T> Maybe<T>.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Maybe<T> {
    return onErrorResumeNext{ throwable : Throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Maybe.empty<T>()
    }
}


fun Completable.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Completable {
    return onErrorResumeNext { throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Completable.complete()
    }
}

fun Completable.doOnLoading(action : (Boolean) -> Unit) : Completable {
    return doOnSubscribe { action(true) }
            .doOnEvent { action(false) }
}

fun <T> Observable<T>.doOnLoading(action : (Boolean) -> Unit) : Observable<T> {
    return doOnSubscribe { action(true) }
            .doOnEach { action(false) }
}

fun <T> Maybe<T>.doOnLoading(action : (Boolean) -> Unit) : Maybe<T> {
    return doOnSubscribe { action(true) }
            .doOnEvent { _, _ -> action(false) }
}

fun <T> Single<T>.toNetWork() : Single<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(30, TimeUnit.SECONDS, AndroidSchedulers.mainThread())

}

fun <T> Maybe<T>.toNetWork() : Maybe<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(30, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .doOnError{err->
                    err.toast()
            }
}
