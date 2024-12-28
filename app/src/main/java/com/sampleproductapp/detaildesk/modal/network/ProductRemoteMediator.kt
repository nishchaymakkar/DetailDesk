@file:OptIn(ExperimentalPagingApi::class)

package com.sampleproductapp.detaildesk.modal.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sampleproductapp.detaildesk.modal.local.ProductDatabase
import com.sampleproductapp.detaildesk.modal.local.ProductEntity
import com.sampleproductapp.detaildesk.modal.mappers.toProductEntity

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val productDb: ProductDatabase,
    private val productApi: DetailDeskApiService
): RemoteMediator<Int, ProductEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType){
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null){
                        1
                    } else{
                        (lastItem.productId/ state.config.pageSize) + 1
                    }
                }
            }

            val products = productApi.getAllProducts(
                page = loadKey.toInt(),
                size = state.config.pageSize
            )
            productDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    productDb.dao.clearAll()
                }
                val productEntities = products.map { it.toProductEntity() }
                productDb.dao.upsertAll(productEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = products.isEmpty()
            )
    } catch (e: Exception){
            MediatorResult.Error(e)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }
}