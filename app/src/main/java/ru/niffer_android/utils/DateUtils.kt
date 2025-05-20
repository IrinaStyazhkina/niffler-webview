package ru.niffer_android.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

     fun convertTimestampToUiDate(isoDate: String): String? {

         val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
         val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

         val zonedDateTime = ZonedDateTime.parse(isoDate, inputFormatter)
         return zonedDateTime.format(outputFormatter)
    }
}