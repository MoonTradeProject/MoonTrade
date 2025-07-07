package com.example.moontrade.model

fun Mode.toWire(): Pair<String, String?> = when (this) {
    Mode.Main              -> "Main"       to null
    is Mode.Tournament     -> "Tournament" to tournamentId
}

fun Mode.tournamentIdOrNull(): String? =
    (this as? Mode.Tournament)?.tournamentId
