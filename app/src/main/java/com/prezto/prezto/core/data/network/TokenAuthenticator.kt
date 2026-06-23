package com.prezto.prezto.core.data.network

import com.prezto.prezto.core.data.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/**
 * Se dispara automáticamente cuando el backend responde 401 Unauthorized.
 * Estructura lista para el refresh de tokens; la llamada real al endpoint queda como TODO.
 */
class TokenAuthenticator @Inject constructor(
    private val sessionManager: SessionManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Evita bucles infinitos: si ya reintentamos con un token, nos rendimos.
        if (responseCount(response) >= 2) return null

        return runBlocking {
            val refreshToken = sessionManager.getRefreshToken() ?: return@runBlocking null

            // TODO(P0): llamar a authApi.refresh(refreshToken), guardar los nuevos tokens con
            //  sessionManager.saveTokens(...) y, si falla, sessionManager.clearSession().
            //  Mientras tanto devolvemos null (deja propagar el 401 y forzar re-login).
            val newAccessToken: String? = null

            newAccessToken?.let {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
