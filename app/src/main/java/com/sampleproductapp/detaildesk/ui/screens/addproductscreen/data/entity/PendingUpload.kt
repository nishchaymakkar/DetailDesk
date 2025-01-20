package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_uploads")
data class PendingUpload(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productName: String,
    val imageUri: String
)
