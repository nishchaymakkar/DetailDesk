package com.sampleproductapp.detaildesk.modal.data

import kotlinx.serialization.SerialName
import java.time.LocalDateTime


data class Product(
    @SerialName("product_id")
    val productId: Long?,
    @SerialName("product_name")
    val productName: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("product_image")
    val productImage: String
)
