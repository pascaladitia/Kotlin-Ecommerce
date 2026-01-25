package com.pascal.feature.shipping

import com.pascal.model.request.ShippingRequest
import com.pascal.model.request.UpdateShippingRequest
import com.pascal.model.response.Shipping

interface ShippingRepository {
    suspend fun createShipping(userId: String, shippingRequest: ShippingRequest): Shipping
    suspend fun getShipping(userId: String, orderId: String): Shipping
    suspend fun updateShipping(userId: String, updateShipping: UpdateShippingRequest): Shipping
    suspend fun deleteShipping(userId: String, id: String):String
}