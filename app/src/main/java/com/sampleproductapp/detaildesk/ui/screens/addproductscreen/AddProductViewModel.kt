package com.sampleproductapp.detaildesk.ui.screens.addproductscreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.sampleproductapp.detaildesk.BuildConfig
import com.sampleproductapp.detaildesk.modal.network.DetailDeskRepository
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.DetailDeskUploadRepository
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.data.entity.PendingUpload
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.di.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val detailDeskUploadRepository: DetailDeskUploadRepository,
    private val detailDeskRepository: DetailDeskRepository
): ViewModel() {

    fun popUp(popUp: () -> Unit) {
        popUp()
    }
    private val _publishEventResult = MutableLiveData<Result<Unit>>(null)
    val publishEventResult: LiveData<Result<Unit>> = _publishEventResult
    private var _imageFileUri: Uri? = null
    val imageFileUri: Uri? get() = _imageFileUri

    // Function to create the camera image file and URI
    fun createCameraImageFile(context: Context): Uri? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )

        _imageFileUri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider", // Use the correct file provider authority
            imageFile
        )

        return _imageFileUri
    }


    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps : StateFlow<Bitmap?> = _bitmaps

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
      _bitmaps.value = null
    }


    var uiState = mutableStateOf(AddProductUiState())
        private set

    private val productName
        get() = uiState.value.productName

    fun onProductNameChange(newValue: String){
        uiState.value = uiState.value.copy(productName = newValue)
    }



    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value = bitmap
    }
    fun clearBitmap() {
        _bitmaps.value = null
    }
    fun publishProductUpdate(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isConnected = NetworkUtils.isNetworkAvailable(context)

                if (!isConnected) {
                    // Handle offline scenario: Save the data locally
                    selectedImageUri.value?.let {
                        val pendingUpload = PendingUpload(
                            productName = productName ?: "",
                            imageUri  = it.toString()
                        )
                        detailDeskUploadRepository.savePendingUpload(pendingUpload)
                        _publishEventResult.value = Result.success(Unit)
                    }


                    onFailure() // Notify the user that the upload is delayed
                } else {

                    // Handle online upload
                    val result =
                        selectedImageUri.value?.let {
                        detailDeskRepository.uploadProductUri(productName,
                            it, context)
                    }
                    if (result!!.isSuccess) {
                        _publishEventResult.value = Result.success(Unit)
                        onSuccess()
                    } else {
                        throw Exception("Upload failed")
                    }
                }
            } catch (e: Exception) {
                _publishEventResult.value = Result.failure(e)
                onFailure()
            } finally {
                _isLoading.value = false
            }
        }
    }

}