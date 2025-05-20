package ru.niffer_android.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.AutoCompleteTextView
import ru.niffer_android.model.CurrencyDropdownItem

object UiFormatter {

    fun setFormatterCurrencyToInput(autoCompleteTextView: AutoCompleteTextView, item: CurrencyDropdownItem) {
        val fullText = "${item.icon}  ${item.text}"

        val spannable = SpannableString(fullText).apply {
            val iconEndIndex = item.icon.length
            val textStartIndex = iconEndIndex + 2

            setSpan(ForegroundColorSpan(Color.BLACK), 0, iconEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(Color.GRAY), textStartIndex, fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        autoCompleteTextView.setText(spannable, false)
    }
}