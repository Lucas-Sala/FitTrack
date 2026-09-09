package com.lucas.fittrack.model

enum class HistoryPeriod(
    val days: Long,
    val displayName: String
) {
    SEVEN_DAYS(
        days = 7,
        displayName = "7 dias"
    ),

    THIRTY_DAYS(
        days = 30,
        displayName = "30 dias"
    )
}