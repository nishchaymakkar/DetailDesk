package com.sampleproductapp.detaildesk.modal.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey
    val productId: Long,
    val productName: String?,
    val createdAt: String?,
    val productImage: String
)
