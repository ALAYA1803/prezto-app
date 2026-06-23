package com.prezto.prezto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.session.AuthState
import com.prezto.prezto.core.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Observa el estado de sesión a nivel de app. La navegación reacciona a sus cambios para
 * mantener el ciclo de vida del Auth coherente (logout o expiración de token => Login).
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    sessionManager: SessionManager
) : ViewModel() {

    val authState: StateFlow<AuthState> = sessionManager.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AuthState.Unknown
        )

    // Punto rojo de la campana. MOCK: arranca con notificaciones pendientes.
    private val _hasNotifications = MutableStateFlow(true)
    val hasNotifications: StateFlow<Boolean> = _hasNotifications.asStateFlow()

    /** Limpia el indicador al abrir el inbox. */
    fun onNotificationsSeen() {
        _hasNotifications.value = false
    }
}

