package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.entity.PendingUpload

@Dao
interface PendingUploadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pendingUpload: PendingUpload)

    @Query("SELECT * FROM pending_uploads")
    suspend fun getAllPendingUploads(): List<PendingUpload>

    @Delete
    suspend fun delete(pendingUpload: PendingUpload)
}
