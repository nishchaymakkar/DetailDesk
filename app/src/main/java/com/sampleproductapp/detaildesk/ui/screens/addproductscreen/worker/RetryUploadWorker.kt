package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.worker

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.DatabaseProvider
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.DetailDeskUploadRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class RetryUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val detailDeskRepository: DetailDeskRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val database = DatabaseProvider.getDatabase(applicationContext)
        val repository = DetailDeskUploadRepository(database.pendingUploadDao())
        val pendingUploads = repository.getPendingUploads()

        pendingUploads.forEach { upload ->
            try {
                val uri = upload.imageUri.toUri()
                val result = detailDeskRepository
                    .uploadProductUri(productName = upload.productName, imageUri = uri, applicationContext)
                if (result.isSuccess) {
                    repository.deletePendingUpload(upload)
                    return Result.success()
                } else {
                    return Result.retry()
                }
            } catch (e: Exception) {
                return Result.failure()
            }
        }
        return Result.success()
    }
}


