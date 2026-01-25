package com.pascal.model.response

import com.pascal.constants.OrderStatus

data class Order(
    val orderId: String,
    val subTotal: Float,
    val total: Float,
    val status: OrderStatus,
)