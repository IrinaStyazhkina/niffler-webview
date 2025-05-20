package ru.niffer_android.utils

import ru.niffer_android.model.CurrencyDropdownItem


object CurrencyUtils {
    fun getCurrencySign(curString: String): String {
        return when (curString) {
            "KZT" -> "₸"
            "RUB" -> "₽"
            "EUR" -> "€"
            "USD" -> "$"
            "ALL" -> "⚖"
            else -> ""
        }
    }

    fun getCurrencyItem(curString: String): CurrencyDropdownItem {
        return CurrencyDropdownItem(text = curString, icon = getCurrencySign(curString))
    }
 }