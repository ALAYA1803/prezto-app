package com.prezto.prezto.core.util.logging

import javax.inject.Inject

/**
 * Logger para builds RELEASE: descarta el ruido (debug) y envía SOLO errores críticos a
 * Firebase Crashlytics, junto con el contexto del usuario.
 *
 * Estructura lista para integrar Firebase. Para activarlo:
 *   1. build.gradle (raíz): plugin `com.google.gms.google-services` y `com.google.firebase.crashlytics`.
 *   2. build.gradle (app): aplicar ambos plugins + `implementation("com.google.firebase:firebase-crashlytics-ktx")`
 *      vía el BoM `platform("com.google.firebase:firebase-bom:...")`.
 *   3. Colocar `google-services.json` en /app.
 *   4. Descomentar las llamadas marcadas abajo.
 */
class CrashlyticsLogger @Inject constructor() : PreztoLogger {

    override fun debug(tag: String, message: String) {
        // Intencionalmente vacío: en release no registramos ruido de debug.
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        // val crashlytics = FirebaseCrashlytics.getInstance()
        // crashlytics.log("$tag: $message")
        // throwable?.let { crashlytics.recordException(it) }
    }

    override fun setUser(userId: String) {
        // FirebaseCrashlytics.getInstance().setUserId(userId)
    }
}
