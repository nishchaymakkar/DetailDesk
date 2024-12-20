package com.sampleproductapp.detaildesk.modal.network

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sampleproductapp.detaildesk.modal.data.Login
import com.sampleproductapp.detaildesk.modal.data.LoginResponse
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.modal.data.SignUp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

class DetailDeskRepository @Inject constructor(
    private val detailDeskApiService: DetailDeskApiService
) {
    suspend fun uploadProductUri(productName:String, imageUri: Uri, context: Context): Result<String> {
        try {
            val contentResolver = context.contentResolver
            val fileInputStream = contentResolver.openInputStream(imageUri) ?: return Result.failure(Exception("File not found"))

            val requestFile = RequestBody.create(
                contentResolver.getType(imageUri)?.toMediaTypeOrNull(),
                fileInputStream.readBytes()
            )
            fileInputStream.close() // Closing after reading

            val uniqueFileName = UUID.randomUUID().toString() + ".jpg"


            val filePart = MultipartBody.Part.createFormData("productImage", uniqueFileName, requestFile)


            val productNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),productName)





            val response = detailDeskApiService.createProduct(
                productNameRequestBody,filePart
            )
            return if (response.isSuccessful) {

                Log.d("API Success", "event created successfully")
                Result.success("isSuccessful")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API Response Error", "Error response: $errorBody")
                Result.failure(Exception("Failed to create event: $errorBody"))
            }


        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    suspend fun uploadProduct(
        productName: String,
        productImage: Bitmap,
        context: Context
    ): Result<String> {
        return try {
            val productNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),productName)
            val productImagePart = bitmapToMultipartBodyPart(productImage, context)

            val response = detailDeskApiService.createProduct(productNameRequestBody, productImagePart)
            if (response.isSuccessful) {
                Result.success("Product uploaded successfully!")
            } else {
                Log.e("DetailDeskRepository", "Upload failed with code: ${response.code()}")
                Result.failure(Exception("Upload failed with code: ${response.code()}"))

            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun bitmapToMultipartBodyPart(bitmap: Bitmap, context: Context): MultipartBody.Part {
        // Create a temporary file in the cache directory
        val file = File(context.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Create RequestBody for the file
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("productImage", file.name, requestBody)
    }




    //sign Up User
    suspend fun registerUser(signUp: SignUp): Result<String> {
        return try {
            val response = detailDeskApiService.registerUser(signUp)
            if (response.isSuccessful) {
                Result.success("User signed up successfully")
            } else {
                Result.failure(Exception("Failed to sign up user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun login(login: Login): Result<LoginResponse>{
        return try {
            val response = detailDeskApiService.login(login)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    Result.success(loginResponse) // Return the Login object on success
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("Failed to sign in user: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    //get all Products
    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val product = detailDeskApiService.getAllProducts()
            Result.success(product)
        }catch (e: IOException) {
            Result.failure(e)
        }catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEventRepository(detailDeskApiService: DetailDeskApiService): DetailDeskRepository {
        return DetailDeskRepository(detailDeskApiService)
    }
}