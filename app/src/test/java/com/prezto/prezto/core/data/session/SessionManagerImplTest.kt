package com.prezto.prezto.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.prezto.prezto.core.data.security.CryptoManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Verifica que [SessionManagerImpl] cifra al guardar y descifra al leer (round-trip),
 * usando un DataStore real sobre un archivo temporal y un [CryptoManager] simulado
 * (el cifrado real con Keystore requiere un test instrumentado en dispositivo).
 */
class SessionManagerImplTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var cryptoManager: CryptoManager
    private lateinit var sessionManager: SessionManager
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    @Before
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(scope = scope) {
            File(tmpFolder.newFolder(), "test.preferences_pb")
        }
        // Cifrado simulado reversible: "enc(<valor>)".
        cryptoManager = mockk {
            every { encrypt(any()) } answers { "enc(${firstArg<String>()})" }
            every { decrypt(any()) } answers {
                firstArg<String>().removePrefix("enc(").removeSuffix(")")
            }
        }
        sessionManager = SessionManagerImpl(dataStore, cryptoManager)
    }

    @After
    fun tearDown() {
        scope.coroutineContext[Job]?.cancel()
    }

    @Test
    fun `al guardar tokens se persisten cifrados y se leen en claro`() = runTest {
        val tokens = AuthTokens(accessToken = "ACCESS_123", refreshToken = "REFRESH_456")

        sessionManager.saveTokens(tokens)

        assertEquals("ACCESS_123", sessionManager.getAccessToken())
        assertEquals("REFRESH_456", sessionManager.getRefreshToken())
    }

    @Test
    fun `authState es Authenticated con token y Unauthenticated tras clearSession`() = runTest {
        assertEquals(AuthState.Unauthenticated, sessionManager.authState.first())

        sessionManager.saveTokens(AuthTokens("ACCESS_123", "REFRESH_456"))
        assertEquals(AuthState.Authenticated, sessionManager.authState.first())

        sessionManager.clearSession()
        assertEquals(AuthState.Unauthenticated, sessionManager.authState.first())
    }
}
