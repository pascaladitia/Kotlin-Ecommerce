package com.pascal.model.response

import com.pascal.constants.PaymentStatus

data class Payment(
    val id: String,
    val orderId: String,
    val amount: Long,
    val status: PaymentStatus,
    val paymentMethod: String,
    val transactionId: String?
)