package com.example.moontrade.utils

import android.util.Log
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Custom Gson adapter that safely parses ISO-8601 strings into java.time.LocalDateTime.
 * On any parse failure it logs the error and returns LocalDateTime.now() to avoid a crash.
 */
class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime =
        try {
            LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            Log.e("LocalDateTimeAdapter", "Failed to parse: ${json.asString}", e)
            LocalDateTime.now()   // fallback value; UI will show a valid date, app won't crash
        }
}
