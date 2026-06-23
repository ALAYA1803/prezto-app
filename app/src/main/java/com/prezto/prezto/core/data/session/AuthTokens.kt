package com.prezto.prezto.core.data.session

/** Par de tokens emitidos por el backend de autenticación. */
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)

/** Estado de sesión derivado de la presencia de un token de acceso válido. */
sealed interface AuthState {
    /** Aún no resuelto (lectura inicial de DataStore en curso). */
    data object Unknown : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}
