package com.sampleproductapp.detaildesk.modal.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProductDao {

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM productentity")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("DELETE FROM productentity")
    suspend fun clearAll()

}