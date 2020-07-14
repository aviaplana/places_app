package com.albertviaplana.chamaasignment.entities

data class Coordinates(
    val latitude: Long,
    val longitude: Long
) {
    override fun toString(): String = "$latitude,$longitude"
}