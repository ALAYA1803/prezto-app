package com.prezto.prezto.feature_explore.presentation.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.feature_explore.domain.model.ItemCondition
import com.prezto.prezto.feature_explore.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PublishState())
    val state: StateFlow<PublishState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
    }

    fun onTitleChanged(title: String) = _state.update { it.copy(title = title, titleError = null) }
    fun onDescriptionChanged(desc: String) = _state.update { it.copy(description = desc, descriptionError = null) }

    fun onDailyRateChanged(rate: String) {
        if (rate.isEmpty() || rate.matches(Regex("^\\d*\\.?\\d*$"))) {
            _state.update { it.copy(dailyRate = rate, dailyRateError = null) }
        }
    }

    fun onCategorySelected(categoryId: String) =
        _state.update { it.copy(selectedCategoryId = categoryId) }

    fun onImageSelected(uri: String?) =
        _state.update { it.copy(selectedImageUri = uri) }

    fun onConditionSelected(condition: ItemCondition) =
        _state.update { it.copy(condition = condition) }

    fun publishTool() {
        val s = _state.value
        var hasError = false

        if (s.title.trim().length < 5) {
            _state.update { it.copy(titleError = "El título debe ser descriptivo (mín. 5 letras)") }
            hasError = true
        }
        if (s.description.trim().length < 20) {
            _state.update { it.copy(descriptionError = "Da más detalles sobre la herramienta (mín. 20 letras)") }
            hasError = true
        }
        if (s.parsedRate == null) {
            _state.update { it.copy(dailyRateError = "Ingresa un precio válido mayor a 0") }
            hasError = true
        }

        if (hasError || s.selectedCategoryId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // Simulación de subida al backend (futuro: PublishToolUseCase + repositorio real).
            delay(1500)
            _state.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }
}
