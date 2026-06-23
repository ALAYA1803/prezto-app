package com.prezto.prezto.core.util.logging

import android.util.Log
import javax.inject.Inject

/**
 * Logger para builds DEBUG: imprime TODO en Logcat. No envía nada a la nube.
 */
class DebugLogger @Inject constructor() : PreztoLogger {

    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    override fun setUser(userId: String) {
        Log.d("PreztoLogger", "setUser($userId)")
    }
}
