package com.sampleproductapp.detaildesk.modal.network

import android.content.Context
import com.sampleproductapp.detaildesk.modal.data.Login
import com.sampleproductapp.detaildesk.modal.data.LoginResponse
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.data.SignUp
import com.sampleproductapp.detaildesk.modal.data.User
import com.sampleproductapp.detaildesk.modal.datastore.DataStoreRepository
import com.sampleproductapp.detaildesk.modal.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

const val  BASE_URL = "http://192.168.1.5:8080/detailDesk/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, dataStoreRepository: DataStoreRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context, dataStoreRepository))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideEventApiService(retrofit: Retrofit): DetailDeskApiService {
        return retrofit.create(DetailDeskApiService::class.java)
    }

}

interface DetailDeskApiService {
    @POST("login")
    suspend fun login(@Body login: Login): Response<LoginResponse>

    @POST("userRegister")
    suspend fun registerUser(@Body signUp: SignUp):Response<Void>

    @Multipart
    @POST("create")
    suspend fun createProduct(
        @Part("productName") productName: RequestBody,
        @Part productImage: MultipartBody.Part
    ):Response<Void>

    @GET("getAll")
    suspend fun getAllProducts() : List<Product>

}