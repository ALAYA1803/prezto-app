package com.prezto.prezto.core.domain.validation

import javax.inject.Inject

/**
 * Validadores de dominio reutilizables (shared kernel). Son UseCases puros, sin dependencias
 * de Android ni de Compose, por lo que son testeables de forma unitaria e inyectables por Hilt
 * en cualquier feature (auth, perfil, publish, etc.).
 */

class ValidateEmail @Inject constructor() {
    // Regex de formato de correo de nivel producción (no depende de android.util.Patterns,
    // lo que la hace testeable en JVM puro).
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(false, "El correo no puede estar vacío")
        }
        if (!emailRegex.matches(email.trim())) {
            return ValidationResult(false, "Ingresa un correo electrónico válido")
        }
        return ValidationResult(true)
    }
}

class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(false, "La contraseña debe tener al menos 8 caracteres")
        }
        if (password.none { it.isDigit() }) {
            return ValidationResult(false, "La contraseña debe contener al menos un número")
        }
        if (password.none { !it.isLetterOrDigit() }) {
            return ValidationResult(false, "La contraseña debe contener al menos un carácter especial")
        }
        return ValidationResult(true)
    }
}

class ValidateName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        if (name.trim().length < 3) {
            return ValidationResult(false, "Ingresa tu nombre completo")
        }
        return ValidationResult(true)
    }
}

class ValidateDni @Inject constructor() {
    operator fun invoke(dni: String): ValidationResult {
        if (dni.any { !it.isDigit() }) {
            return ValidationResult(false, "El DNI solo debe contener números")
        }
        if (dni.length != 8) {
            return ValidationResult(false, "El DNI debe tener 8 dígitos")
        }
        return ValidationResult(true)
    }
}

class ValidatePhone @Inject constructor() {
    operator fun invoke(phone: String): ValidationResult {
        if (phone.any { !it.isDigit() }) {
            return ValidationResult(false, "El celular solo debe contener números")
        }
        if (phone.length != 9) {
            return ValidationResult(false, "Ingresa un celular válido (9 dígitos)")
        }
        return ValidationResult(true)
    }
}
