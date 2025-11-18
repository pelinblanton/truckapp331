package com.example.truckapp331.model

data class Delivery(
    val id: Int,
    val name: String,
    val instructions: String,
    val quantity: Int,
    val time: String,
    val isCompleted: Boolean = false,
    val startTime: Long? = null
)
