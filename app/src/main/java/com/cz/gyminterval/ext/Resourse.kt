package com.cz.gyminterval.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Created by cz on 3/28/18.
 */
fun Context.getTintedDrawable(@DrawableRes drawableRes: Int, @ColorInt tintColor: Int) =
        DrawableCompat.wrap(getDrawableCompat(drawableRes)).apply {
            DrawableCompat.setTint(this, tintColor)
        }

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)
fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable = ContextCompat.getDrawable(this, drawableRes)!!

fun Int.toColorValue(context: Context) = context.getColorCompat(this)
fun Int.toDimen(context: Context) = context.resources.getDimension(this)
fun Int.toFormattedString(context: Context, vararg args: Any?) = context.getString(this, *args)

object DateConstants {
    const val ONE_HOUR_MILLS = 3600 * 1000L
    const val ONE_DAY_MILLS = 24 * ONE_HOUR_MILLS
}


