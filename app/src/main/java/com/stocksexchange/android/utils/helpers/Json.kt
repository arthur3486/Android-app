package com.stocksexchange.android.utils.helpers

import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Converts a json string into a JsonObject and returns it or null
 * if the JSON is invalid.
 *
 * @param jsonString The json string to parse
 *
 * @return The instance of JsonObject or null if JSON is invalid
 */
fun fromJsonStringToJsonObject(jsonString: String): JsonObject? {
    return try {
        (JsonParser().parse(jsonString) as JsonObject)
    } catch(exception: Throwable) {
        null
    }
}