package com.cz.gyminterval.ext

import android.app.Activity

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cz.gyminterval.BaseApp
import com.cz.gyminterval.R


import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun EditText.isEmpty() = text.isEmpty()

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layout, this, attachToRoot)

inline fun <reified T : View> Activity.findView(@IdRes id: Int) = findViewById<T>(id) as T
inline fun <reified T : View> View.findView(@IdRes id: Int) = findViewById<T>(id) as T

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("show", "finalVisibility", requireAll = false)
fun View.setVisible(visible: Boolean, finalVisibility: Int) {
    val visibility = if (finalVisibility == 0) View.GONE else finalVisibility
    this.visibility = if (visible) View.VISIBLE else visibility
}


@BindingAdapter("touchAble")
fun setClickAndForce(view: View, able: Boolean) {
    view.isClickable = able
    view.isFocusable = able
}


@BindingAdapter("animationRes")
fun setAnimationRes(view: View, drawable: Drawable) {
    if (view is ImageView) {
        view.setImageDrawable(drawable)
        if (view.drawable is AnimationDrawable)
            (view.drawable as AnimationDrawable).start()
    } else {
        view.background = drawable
        if (view.background is AnimationDrawable)
            (view.background as AnimationDrawable).start()
    }
}


//给一个 view设置 touchlistener
@BindingAdapter("touchListener")
fun setTouchListener(view: View, onTouchListener: View.OnTouchListener) {
    view.setOnTouchListener(onTouchListener)
}

//给一个 view设置 onClickListener
@BindingAdapter("onClickListener")
fun setClickListener(view: View, onclick: () -> Unit) {
    view.setOnClickListener { onclick() }
}


@BindingAdapter("checkAble", "isMaxSelect")
fun setShow(view: CheckedTextView, checkAble: Boolean, isMaxSelect: Boolean) {
    if (checkAble && (!isMaxSelect || view.isChecked))
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}


var View.show: Boolean
    set(value) = if (value) visibility = View.VISIBLE else visibility = View.GONE
    get() = visibility == View.VISIBLE

@set:BindingAdapter("string")
@get:InverseBindingAdapter(attribute = "string")
var EditText.string: String
    set(value) {
        setText(value)
    }
    get() = text.toString()


@BindingAdapter("minWidth")
fun setViewMinWidth(view: View, width: Float) {
    view.minimumWidth = width.toInt()
}

@BindingAdapter("minHeight")
fun setViewMinHeight(view: View, height: Float) {
    view.minimumHeight = height.toInt()
}

@BindingAdapter("url")
fun setImageUrl(view: ImageView, url: String?) {
    var complete = url ?: return
    if (url.startsWith("http").not()) {
        complete = url
    }
    Glide.with(view)
            .load(complete)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(view)
}

@BindingAdapter("circleImage")
fun setCircleImageUrl(view: ImageView, url: String?) {
    Glide.with(view)
            .load(url)
            .apply(
                RequestOptions.bitmapTransform(CircleCrop()).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .into(view)
}

@BindingAdapter("roundedCorners")
fun setRoundedCornersImageUrl(view: ImageView, url: String?) {
    var complete = url ?: return
    if (url.startsWith("http").not()) {
        complete = url
    }

    Glide.with(view)
            .load(complete)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .into(view)
}


@BindingAdapter("animationSrc", "loop", requireAll = false)
fun setImageAnimatedSrc(view: ImageView, drawable: Drawable, loop: Boolean? = false) {
    view.setImageDrawable(drawable)
    if (drawable is AnimationDrawable) {
        drawable.start()
    }

    if (drawable is AnimatedVectorDrawable) {
        drawable.start()

        if (loop == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            drawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    (drawable as? AnimatedVectorDrawable)?.start()
                }
            })
    }
}


fun View.observeLayoutChanges(): io.reactivex.rxjava3.core.Observable<View> {
    return io.reactivex.rxjava3.core.Observable.create { emitter ->
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            emitter.onNext(this)
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)
        emitter.setCancellable { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}


private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
@BindingAdapter("date")
fun TextView.setDate(time: Long) {
    text = simpleDateFormat.format(Date(time))
}

private val explicitFormat = SimpleDateFormat("MM-dd HH:mm:ss")
@BindingAdapter("explicitDate")
fun TextView.setExplicitDate(time: Long) {
    text = explicitFormat.format(Date(time))
}

//@BindingAdapter("listener")
//fun View.setListener(listener: Any?) {
//    when {
//        this is SmartRefreshLayout && listener is RefreshListener -> {
//            setOnRefreshListener(listener::onRefresh)
//            setOnLoadmoreListener(listener::onLoadMore)
//        }
//
//        this is SwipeRefreshLayout && listener is SampleRefreshListener -> {
//            setOnRefreshListener(listener)
//        }
//    }
//
//}

@BindingAdapter("loadStorageImg")
fun ImageView.setLoadStorageImg(path: String?) {
    when {
        path == null -> return
        path.startsWith("/") || File(path).exists() -> Glide.with(this).load(path).into(this)
        path.startsWith("content") || path.startsWith("file") -> Glide.with(this).load(context.fromUriToFilePath(Uri.parse(path))).into(this)
        else -> setImageUrl(this, path)
    }
}

