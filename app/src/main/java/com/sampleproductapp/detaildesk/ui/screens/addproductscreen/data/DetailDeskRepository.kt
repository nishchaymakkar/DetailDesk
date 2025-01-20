package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data

import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.entity.PendingUpload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailDeskUploadRepository @Inject constructor(
    private val pendingUploadDao: PendingUploadDao
) {
    suspend fun savePendingUpload(pendingUpload: PendingUpload) {
        pendingUploadDao.insert(pendingUpload)
    }

    suspend fun getPendingUploads(): List<PendingUpload> {
        return pendingUploadDao.getAllPendingUploads()
    }
    suspend fun deletePendingUpload(pendingUpload: PendingUpload) {
        return pendingUploadDao.delete(pendingUpload)
    }
}
