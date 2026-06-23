package com.prezto.prezto.feature_support.presentation.report

data class ReportIncidentState(
    val problemType: String? = null,
    val description: String = "",
    val photoUri: String? = null,
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false
) {
    val isValid: Boolean
        get() = problemType != null && description.trim().length >= 10

    companion object {
        val PROBLEM_TYPES = listOf(
            "Falla de funcionamiento",
            "Daño físico",
            "No coincide con la publicación",
            "Problema con el propietario",
            "Otro"
        )
    }
}
