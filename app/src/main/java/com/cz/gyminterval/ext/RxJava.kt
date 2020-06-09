package com.xzrf.keypersonnel.ext



import com.cz.gyminterval.ext.e
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("RxJava")

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

/***
 * 注意这次捕捉异常，会返回一次执行成功
 * ***/
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

fun <T> Single<T>.doOnLoading(action : (Boolean) -> Unit) : Single<T> {
    return doOnSubscribe { action(true) }
            .doOnEvent { _, _ -> action(false) }
}