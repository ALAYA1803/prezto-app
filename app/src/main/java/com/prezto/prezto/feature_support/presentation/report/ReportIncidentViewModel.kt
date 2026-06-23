package com.prezto.prezto.feature_support.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportIncidentViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ReportIncidentState())
    val state: StateFlow<ReportIncidentState> = _state.asStateFlow()

    fun onTypeSelected(type: String) = _state.update { it.copy(problemType = type) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }
    fun onPhotoSelected(uri: String?) = _state.update { it.copy(photoUri = uri ?: it.photoUri) }

    fun submit() {
        val s = _state.value
        if (!s.isValid || s.isSubmitting) return
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }
            // TODO(P0): SupportRepository.submitIncident(type, description, photoUri).
            delay(1500)
            _state.update { it.copy(isSubmitting = false, isSubmitted = true) }
        }
    }
}
