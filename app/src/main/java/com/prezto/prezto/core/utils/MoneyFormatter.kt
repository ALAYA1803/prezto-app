package com.prezto.prezto.core.utils

import java.util.Locale

/** Formatea un monto como moneda Prezto, ej. 25.0 -> "S/ 25.00". */
fun Double.toSoles(): String =
    "${Constants.CURRENCY_SYMBOL} ${"%.2f".format(Locale.US, this)}"
