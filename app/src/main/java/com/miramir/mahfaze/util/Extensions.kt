package com.miramir.mahfaze.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun <T> Call<T>.executeAndUnwrap(): T {
    val response: Response<T>

    try {
        response = execute()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    @Suppress("UNCHECKED_CAST")
    if (response.isSuccessful) return response.body() ?: Unit as T

    throw HttpException(response)
}

fun <T> Call<T>.enqueueAndUnwrap(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                @Suppress("UNCHECKED_CAST")
                onSuccess(response.body() ?: Unit as T)
            } else {
                onError(HttpException(response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(t)
        }
    })
}

fun Context.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService<InputMethodManager>()
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.hideSoftKeyboard(view: View) {
    val imm = getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}
