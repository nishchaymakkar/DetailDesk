@file:OptIn(ExperimentalEncodingApi::class)

package com.sampleproductapp.detaildesk.modal.mappers

import android.content.Context
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.local.ProductEntity
import com.sampleproductapp.detaildesk.modal.network.ProductDto
import java.io.File
import android.util.Base64

import kotlin.io.encoding.ExperimentalEncodingApi


fun ProductDto.toProductEntity() = ProductEntity(
    productId = this.productId,
    productName = this.productName,
    createdAt = this.createdAt,
    productImage = this.productImage
)

fun ProductEntity.toProduct() = Product(
    productId = this.productId,
    productName = this.productName,
    createdAt = this.createdAt,
    productImage =this.productImage

)

fun ProductDto.toProductEntity(context: Context): ProductEntity {
    val fileName = "product_${this.productId}.jpg"
    val file = File(context.filesDir, fileName)

    if (!file.exists()) {
        val imageBytes = safeBase64Decode(this.productImage) // <-- replace with correct field name
        imageBytes?.let {
            file.writeBytes(it)
        }
    }

    return ProductEntity(
        productId = this.productId,
      productName = this.productName,
        createdAt = this.createdAt,
        productImage = file.absolutePath
    )
}
fun safeBase64Decode(base64: String): ByteArray? {
    return try {
        Base64.decode(base64, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}
