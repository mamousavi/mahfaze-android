package com.miramir.mahfaze.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {
    private class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

    val IO_EXECUTOR: Executor = Executors.newSingleThreadExecutor()

    val MAIN_EXECUTOR: Executor = MainThreadExecutor()
}