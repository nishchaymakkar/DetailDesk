package com.sampleproductapp.detaildesk

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.worker.RetryUploadWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DetailDeskApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: RetryWorkerFactory


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

class RetryWorkerFactory@Inject constructor(
    private val detailDeskRepository: DetailDeskRepository
): WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = RetryUploadWorker(
        context = appContext,
        workerParams = workerParameters,
        detailDeskRepository = detailDeskRepository
    )
}