package com.prezto.prezto.feature_auth.presentation.verification

data class VerificationState(
    val phone: String = "",
    val otp: String = "",
    val otpError: String? = null,
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false,
    /** Segundos restantes para poder reenviar el código (0 = se puede reenviar). */
    val secondsRemaining: Int = 0
) {
    val canResend: Boolean get() = secondsRemaining == 0 && !isVerifying
    val isOtpComplete: Boolean get() = otp.length == OTP_LENGTH

    companion object {
        const val OTP_LENGTH = 6
    }
}
