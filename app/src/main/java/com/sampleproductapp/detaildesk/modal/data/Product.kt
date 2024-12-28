package com.sampleproductapp.detaildesk.modal.data

import kotlinx.serialization.SerialName
import java.time.LocalDateTime


data class Product(

    val productId: Long,
    val productName: String?,
    val createdAt: String?,
    val productImage: String
)
