package com.example.truckapp331

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.truckapp331.model.Delivery
import com.example.truckapp331.model.sampleDeliveries

class DeliveryViewModel : ViewModel() {

    val allDeliveries = mutableStateListOf<Delivery>().apply {
        addAll(sampleDeliveries)
    }

    fun getDeliveryById(id: Int): Delivery? {
        return allDeliveries.find { it.id == id }
    }

    fun getCompletedDeliveries(): List<Delivery> {
        return allDeliveries.filter { it.isCompleted }
    }

    fun getActiveDeliveries(): List<Delivery> {
        return allDeliveries.filter { !it.isCompleted }
    }

    fun markDeliveryAsCompleted(delivery: Delivery) {
        val index = allDeliveries.indexOfFirst { it.id == delivery.id }
        if (index != -1) {
            allDeliveries[index] = delivery.copy(isCompleted = true)
        }
    }
    fun startDelivery(deliveryId: Int) {
        val index = allDeliveries.indexOfFirst { it.id == deliveryId }
        if (index != -1) {
            val updated = allDeliveries[index].copy(startTime = System.currentTimeMillis())
            allDeliveries[index] = updated
        }
    }
}

