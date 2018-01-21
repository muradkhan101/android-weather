package com.khanmurad.weather.WeatherAPI

import kotlinx.coroutines.experimental.CancellableContinuation
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Suppress("unused") val Any?.unit get() = Unit

operator fun StringBuilder.plusAssign(string: String) = append(string).unit

fun <T> List<T>.changes(destination: MutableList<Pair<T, T>> = ArrayList(size))
        : MutableList<Pair<T, T>> {
    for (i in 0..size - 2)
        destination += get(i) to get(i + 1)
    return destination
}

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { continuation ->
    continuation.invokeOnCompletion { if (continuation.isCancelled) cancel() }
    val callback = object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) = continuation.tryToResume { throw t }
        override fun onResponse(call: Call<T>, response: Response<T>) = continuation.tryToResume {
            response.isSuccessful || throw IllegalStateException("Http error ${response.code()}")
            response.body() ?: throw IllegalStateException("Response body is null")
        }
    }
    enqueue(callback)
}

private inline fun <T> CancellableContinuation<T>.tryToResume(getter: () -> T) {
    isActive || return
    try { resume(getter()) }
    catch (exception: Throwable) { resumeWithException(exception) }
}