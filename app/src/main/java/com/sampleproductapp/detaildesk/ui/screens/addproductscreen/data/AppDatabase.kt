package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.entity.PendingUpload

@Database(entities = [PendingUpload::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pendingUploadDao(): PendingUploadDao
}
