package ru.niffer_android.model

enum class Period (val uiName: String, val apiValue: String) {
    LAST_MONTH("Last month", "MONTH"),
    LAST_WEEK("Last week", "WEEK"),
    TODAY("Today", "TODAY"),
    ALL_TIME("All time", "");

    companion object {
        fun getPeriodByUiName(name: String): Period  {
            return requireNotNull( Period.entries.find { it.uiName == name })
        }
    }
}