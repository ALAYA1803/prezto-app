package com.prezto.prezto.core.data.network

import com.prezto.prezto.core.data.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Añade el header "Authorization: Bearer <token>" a cada petición saliente cuando hay sesión.
 * La lectura del token es puntual y rápida (DataStore en memoria); runBlocking es seguro aquí
 * porque OkHttp ya ejecuta en un hilo de I/O en background.
 */
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking { sessionManager.getAccessToken() }

        val request = if (token.isNullOrBlank()) {
            original
        } else {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(request)
    }
}
