package com.prezto.prezto.core.data.session

import kotlinx.coroutines.flow.Flow

/**
 * Fuente única de verdad de la sesión del usuario. Abstrae el almacenamiento cifrado
 * para que el resto de la app (Splash, interceptores, logout) dependa de esta interfaz
 * y no de DataStore/Keystore directamente. Testeable con un fake en memoria.
 */
interface SessionManager {

    /** Estado de sesión observable, derivado del token persistido. */
    val authState: Flow<AuthState>

    suspend fun saveTokens(tokens: AuthTokens)

    suspend fun clearSession()

    /** Lectura puntual del access token (usada por el AuthInterceptor). */
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?
}
