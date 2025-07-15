package com.example.moontrade.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject

sealed class Mode {

    data object Main : Mode()

    data class Tournament(val tournamentId: String) : Mode()

    fun toJson(): JsonElement {
        return when (this) {
            is Main -> com.google.gson.JsonPrimitive("main")
            is Tournament -> JsonObject().apply {
                add("tournament", JsonObject().apply {
                    addProperty("tournament_id", tournamentId)
                })
            }
        }
    }

    fun toQueryMap(): Map<String, String> {
        return when (this) {
            is Mode.Main -> mapOf("mode" to "main")
            is Mode.Tournament -> mapOf(
                "mode" to "tournament",
                "tournament_id" to tournamentId
            )
        }
    }


    companion object {
        fun fromJson(json: JsonElement): Mode? {
            return when {
                json.isJsonPrimitive && json.asString == "main" -> Main
                json.isJsonObject && json.asJsonObject.has("tournament") -> {
                    val tObj = json.asJsonObject.getAsJsonObject("tournament")
                    val id = tObj.get("tournament_id").asString
                    Tournament(id)
                }
                else -> null
            }
        }
    }


}
