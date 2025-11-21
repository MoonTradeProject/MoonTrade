package com.example.moontrade.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs



private fun symbols() = DecimalFormatSymbols.getInstance(Locale.US)
private fun cryptoDf() = DecimalFormat("#,##0.########", symbols()).apply {
    isParseBigDecimal = true
    minimumFractionDigits = 2
}
private fun fiatDf()   = DecimalFormat("#,##0.00",       symbols()).apply { isParseBigDecimal = true  }
private fun percentDf(decimals: Int) = DecimalFormat("#,##0.${"0".repeat(decimals)}", symbols())

private val MIN_CRYPTO = BigDecimal("0.00000001")
private fun BigDecimal.stripMinusZero(): BigDecimal =
    if (this.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO else this

/** Crypto: till 8 symbols */
fun formatCryptoAmount(amount: BigDecimal): String {
    val a = amount.stripTrailingZeros().stripMinusZero()
    return when {
        a == BigDecimal.ZERO -> "0"
        a > BigDecimal.ZERO && a < MIN_CRYPTO ->
            "< ${MIN_CRYPTO.stripTrailingZeros().toPlainString()}"
        else -> cryptoDf().format(a)
    }
}

/** USD: 2 symbols, HALF_UP. */
fun formatFiat(amountUsd: BigDecimal): String =
    fiatDf().format(amountUsd.setScale(2, RoundingMode.HALF_UP))

/** %: without -0.00 */
fun formatPercent(value: Double, keepPlus: Boolean = true, decimals: Int = 2): String {
    val fixed = if (abs(value) < 1e-7) 0.0 else value
    val body = percentDf(decimals).format(abs(fixed))
    val sign = when {
        fixed > 0 && keepPlus -> "+"
        fixed < 0 -> "-"
        else -> ""
    }
    return "$sign$body%"
}

/** parse what came as a string (with $, spaces, commas, etc.) */
fun parseBigDecimalLoose(src: String?): BigDecimal? {
    if (src.isNullOrBlank()) return null
    // remove everything except numbers, symbols, and separators
    val cleaned = src.replace(Regex("[^0-9,.-]"), "")
    if (cleaned.isBlank()) return null

    // if there are more commas than periods, we assume that comma = decimal
    val decimalIsComma = cleaned.count { it == ',' } > cleaned.count { it == '.' }
    val normalized = if (decimalIsComma) {
        cleaned.replace(".", "").replace(',', '.')
    } else {
        cleaned.replace(",", "")
    }
    return normalized.toBigDecimalOrNull()
}

/** USD full format: $12,345.67 */
fun formatUsdFull(amount: BigDecimal): String = "$" + formatFiat(amount)

/** USD compact: like $1.98K / $2.34M / $5.67B */
fun formatUsdCompact(amount: BigDecimal): String {
    val abs = amount.abs()
    val (div, suffix) = when {
        abs >= BigDecimal("1000000000") -> BigDecimal("1000000000") to "B"
        abs >= BigDecimal("1000000")    -> BigDecimal("1000000")    to "M"
        abs >= BigDecimal("1000")       -> BigDecimal("1000")       to "K"
        else -> BigDecimal.ONE to ""
    }
    val scaled = amount.divide(div, 2, RoundingMode.HALF_UP)
    return "$" + scaled.stripTrailingZeros().toPlainString() + suffix
}
