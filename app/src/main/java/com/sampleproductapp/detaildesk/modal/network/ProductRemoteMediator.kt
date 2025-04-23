@file:OptIn(ExperimentalPagingApi::class)

package com.sampleproductapp.detaildesk.modal.network

import android.content.Context
import android.util.Log
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
    private val context: Context,
    private val productDb: ProductDatabase,
    private val productApi: DetailDeskApiService
): RemoteMediator<Int, ProductEntity>() {
    
    // Add page tracking
    private var currentPage = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> {
                    currentPage = 0
                    currentPage
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        // Calculate next page based on total items loaded
                        (state.pages.size + 1)
                    }
                }
            }

            val products = productApi.getAllProducts(
                page = loadKey,
                size = state.config.pageSize
            )
            
            productDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    productDb.dao.clearAll()
                }
                val productEntities = products.map { it.toProductEntity(context = context) }
                productDb.dao.upsertAll(productEntities)
            }

            // Update current page if successful
            if (loadType == LoadType.APPEND) {
                currentPage = loadKey
            }

            MediatorResult.Success(
                endOfPaginationReached = products.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}