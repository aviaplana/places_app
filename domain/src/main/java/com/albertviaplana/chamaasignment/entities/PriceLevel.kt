package com.albertviaplana.chamaasignment.entities

enum class PriceLevel(val level: Int) {
    FREE(0),
    INEXPENSIVE(1),
    MODERATE(2),
    EXPENSIVE(3),
    VERY_EXPENSIVE(4);

    companion object {
        private val values = values()

        fun getByValue(value: Int) = values.firstOrNull { it.level == value } ?: FREE
    }
}

