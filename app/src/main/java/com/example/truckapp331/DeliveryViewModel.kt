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

    fun markDeliveryAsCompleted(delivery: Delivery) {
        val index = allDeliveries.indexOfFirst { it.id == delivery.id }
        if (index != -1) {
            // ✅ Replace the item to trigger recomposition
            allDeliveries[index] = delivery.copy(isCompleted = true)
        }
    }
}

