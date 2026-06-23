package com.prezto.prezto.core.util.logging

/**
 * Abstracción de logging/observabilidad. Desacopla la app de la implementación concreta
 * (Logcat en debug, Crashlytics en release), por lo que el resto del código nunca depende
 * de Firebase directamente y es 100% testeable.
 */
interface PreztoLogger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)

    /** Asocia el usuario actual a los reportes (contexto para el CTO en producción). */
    fun setUser(userId: String)
}
