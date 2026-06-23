package com.prezto.prezto.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.prezto.prezto.core.data.security.CryptoManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [SessionManager] sobre DataStore Preferences. Los tokens se cifran con
 * [CryptoManager] (Keystore) ANTES de persistirse, de modo que en disco nunca hay texto plano.
 */
@Singleton
class SessionManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) : SessionManager {

    override val authState: Flow<AuthState> = dataStore.data
        .catch { exception ->
            // Si el archivo de DataStore se corrompe, lo tratamos como sesión vacía.
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs ->
            val token = prefs[ACCESS_TOKEN]?.let { cryptoManager.decrypt(it) }
            if (token.isNullOrBlank()) AuthState.Unauthenticated else AuthState.Authenticated
        }

    override suspend fun saveTokens(tokens: AuthTokens) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = cryptoManager.encrypt(tokens.accessToken)
            prefs[REFRESH_TOKEN] = cryptoManager.encrypt(tokens.refreshToken)
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    override suspend fun getAccessToken(): String? =
        readAndDecrypt(ACCESS_TOKEN)

    override suspend fun getRefreshToken(): String? =
        readAndDecrypt(REFRESH_TOKEN)

    private suspend fun readAndDecrypt(key: Preferences.Key<String>): String? {
        val prefs = dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .first()
        return prefs[key]?.let { cryptoManager.decrypt(it) }
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
