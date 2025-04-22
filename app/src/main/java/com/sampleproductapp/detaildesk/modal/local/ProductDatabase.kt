package com.sampleproductapp.detaildesk.modal.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 2
)
abstract class ProductDatabase: RoomDatabase() {

    abstract val dao: ProductDao
}