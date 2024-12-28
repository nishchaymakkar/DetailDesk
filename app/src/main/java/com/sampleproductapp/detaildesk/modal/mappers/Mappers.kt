package com.sampleproductapp.detaildesk.modal.mappers

import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.local.ProductEntity
import com.sampleproductapp.detaildesk.modal.network.ProductDto


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