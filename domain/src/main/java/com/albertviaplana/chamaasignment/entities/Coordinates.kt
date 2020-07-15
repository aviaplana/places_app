package com.albertviaplana.chamaasignment.entities

data class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String = "$latitude,$longitude"
}