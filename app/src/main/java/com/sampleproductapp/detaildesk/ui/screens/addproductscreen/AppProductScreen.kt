@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.sampleproductapp.detaildesk.ui.screens.addproductscreen

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sampleproductapp.detaildesk.BuildConfig
import com.sampleproductapp.detaildesk.R
import com.sampleproductapp.detaildesk.ui.components.BasicButton
import com.sampleproductapp.detaildesk.ui.components.ProductNameTextField
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.di.NetworkUtils
import com.sampleproductapp.detaildesk.ui.screens.addproductscreen.worker.RetryUploadWorker
import kotlin.time.Duration
import kotlin.time.DurationUnit


@Composable
fun AddProductScreen(
    modifier: Modifier,
    popUp: () -> Unit,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    val uploading by viewModel.isLoading
    val context = LocalContext.current
    val publishEventResult by viewModel.publishEventResult.observeAsState()
    LaunchedEffect(publishEventResult) {
        publishEventResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Product Upload Successfully", Toast.LENGTH_SHORT).show()
                viewModel.popUp(popUp)
            } else {
                Toast.makeText(context, "Failed to Upload Product", Toast.LENGTH_SHORT).show()
            }
        }

        //viewModel.enqueueRetryWorker(context)
        NetworkUtils.registerNetworkCallback(
            context = context,
            onNetworkAvailable = {
                val workRequest = OneTimeWorkRequestBuilder<RetryUploadWorker>()
                    .build()
                // Retry uploads when the network becomes available
                WorkManager.getInstance(context).enqueue(
                    workRequest
                )

            },
            onNetworkLost = {
                // Handle network lost
            }
        )
    }

    val uiState by viewModel.uiState
    AddProductScreenContent(
        isUploading = uploading,
        modifier = modifier,
        uiState =  uiState,
        onProductNameChange = viewModel::onProductNameChange,
        popUp = popUp,
        publishUpdateClick = {viewModel.publishProductUpdate(context, onSuccess = {}, onFailure = {})}
        )

}

@Composable
fun AddProductScreenContent(
    isUploading: Boolean,
    modifier: Modifier,
    popUp: () -> Unit,
    uiState: AddProductUiState ,
    onProductNameChange: (String) -> Unit,
    publishUpdateClick: () -> Unit
) {

    Scaffold(
        modifier = modifier,
//        topBar = {
//            TopAppBar(
//                colors = TopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.onPrimary,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
//                    actionIconContentColor = MaterialTheme.colorScheme.primary,
//                    scrolledContainerColor = MaterialTheme.colorScheme.onPrimary
//
//                ),
//                title = { },
//                navigationIcon = {
//                    IconButton(
//                        onClick = {popUp()}
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
//                            contentDescription = null
//                        )
//                    }
//                },
//
//            )
//        },
        content = { it ->
            Box(
                modifier = Modifier.padding(it),
                contentAlignment = Alignment.Center,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = {popUp()},
                            modifier = Modifier
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                    Column(Modifier.padding(top = 50.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ImageSelectorUI()
                        ProductNameTextField(value = uiState.productName,
                            onNewValue =onProductNameChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        BasicButton(text = R.string.add_product,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            action = {
                                publishUpdateClick()
                        })
                    }
                    if (isUploading) { CircularProgressIndicator()   }

                }
            )
        }
    )


}



@Composable
fun ImageSelectorUI(
    viewModel: AddProductViewModel = hiltViewModel()
) {

    var isSheetOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageUri by viewModel.selectedImageUri.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState()


    val uriByCamera = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
           viewModel.onImageSelected(uri)
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()){
                viewModel.onImageSelected(uriByCamera.value)

            }

    val camPermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                uriByCamera.value = viewModel.createCameraImageFile(context)
                uriByCamera.value?.let { uri -> cameraLauncher.launch(uri)}
            } else {
                Toast.makeText(context, "Permission was not granted", Toast.LENGTH_SHORT)
                    .show()

            }
        }

    val storagePermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission was not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    Box(
        modifier = Modifier.padding(vertical = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {


            if (imageUri != null) {
                Box(
                    modifier = Modifier
                        .aspectRatio(16 / 9f)
                        .padding(horizontal = 8.dp)

                ){
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(16 / 9f)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { isSheetOpen = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Gray.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Image",
                            tint = Color.White
                        )
                    }

                }

            }


            else {
            Card (
                Modifier.clickable(
                    onClick = {
                        isSheetOpen = true
                    }
                ).padding(horizontal = 16.dp)
            ){
                Box(
                    modifier = Modifier
                        .aspectRatio(16 / 9f)
                        .padding(horizontal = 16.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center,
                    content = {

                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Add Image",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    "Tap to select an image",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                    }
                )
            }


        }




    }
    if (isSheetOpen) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            sheetState = bottomSheetState,
            onDismissRequest = { isSheetOpen = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Image Source",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Camera Option
                    IconButton(
                        onClick = {
                            camPermission.launch(android.Manifest.permission.CAMERA)
                            isSheetOpen = false
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    // Image Picker Option
                    IconButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                              storagePermission.launch(
                                   android.Manifest.permission.READ_MEDIA_IMAGES
                                )
                            } else {
                                storagePermission.launch(
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            isSheetOpen = false
                        }},
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = "Image Picker",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}



