package com.cz.gyminterval.ext

import java.util.*


@Suppress("NOTHING_TO_INLINE")
inline fun <T> T?.toOptional() : Optional<T> {
    return Optional.ofNullable(this)
}

val <T> Optional<T>.isAbsent : Boolean
get() = isPresent.not()