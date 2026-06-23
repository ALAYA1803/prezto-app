package com.prezto.prezto.feature_explore.presentation.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.location.LocationProvider
import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.Owner
import com.prezto.prezto.feature_explore.domain.usecase.GetCategoriesUseCase
import com.prezto.prezto.feature_explore.domain.usecase.PublishItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val publishItemUseCase: PublishItemUseCase,
    private val locationProvider: LocationProvider
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

    /** Captura la ubicación actual como ubicación del artículo (tras conceder el permiso). */
    fun captureCurrentLocation() {
        viewModelScope.launch {
            val location = locationProvider.getCurrentLocation() ?: return@launch
            _state.update { it.copy(latitude = location.latitude, longitude = location.longitude) }
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

    fun onConditionSelected(condition: com.prezto.prezto.feature_explore.domain.model.ItemCondition) =
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

        if (hasError || s.selectedCategoryId == null || s.latitude == null || s.longitude == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val item = Item(
                id = "item_${System.currentTimeMillis()}",
                categoryId = s.selectedCategoryId,
                title = s.title.trim(),
                description = s.description.trim(),
                dailyRate = s.parsedRate!!,
                hourlyRate = s.parsedRate!! / 5.0,
                currentCondition = s.condition,
                isAvailable = true,
                imageUrl = s.selectedImageUri.orEmpty(),
                owner = currentOwner(),
                latitude = s.latitude,
                longitude = s.longitude
            )
            publishItemUseCase(item)
            _state.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }

    // MOCK: el propietario actual vendrá de un UserRepository real.
    private fun currentOwner(): Owner =
        Owner(id = "usr_99", name = "Rodrigo A.", trustScore = TrustScore(4.9), isVerified = true)
}
