package com.sampleproductapp.detaildesk.ui.screens.addproductscreen.di

import android.content.Context
import androidx.room.Room
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.AppDatabase
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.PendingUploadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun providePendingUploadDao(database: AppDatabase): PendingUploadDao {
        return database.pendingUploadDao()
    }
}
