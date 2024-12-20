@file:OptIn(ExperimentalPermissionsApi::class)

package com.sampleproductapp.detaildesk.ui.screens.addproductscreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@Composable
fun UnifiedImageCapture(
    modifier: Modifier = Modifier,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("Camera") }

    Column(modifier = modifier) {
        // Tab Row to toggle between Camera and Image Picker
        TabRow(
            selectedTabIndex = if (selectedTab == "Camera") 0 else 1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == "Camera",
                onClick = { selectedTab = "Camera" },
                text = { Text("Camera") }
            )
            Tab(
                selected = selectedTab == "Image Picker",
                onClick = { selectedTab = "Image Picker" },
                text = { Text("Image Picker") }
            )
        }

        // Content based on selected tab
        when (selectedTab) {
            "Camera" -> {
                CameraScreen(
                    modifier = Modifier,
                    viewModel = viewModel
                )
            }

            "Image Picker" -> {
                ImagePickerCard(viewModel = viewModel)
            }
        }
    }
}


@ExperimentalPermissionsApi
@Composable
fun ImagePickerWithPermissions(openCamera: Boolean, openImagePicker: Boolean) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    // Permission state for reading external storage
    val storagePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = android.Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        rememberPermissionState(
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    if (cameraPermissionState.status.isGranted) {
        // Camera permission granted
    } else {
        // Request camera permission
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }


    // Check permission status
    //  if(openImagePicker){
    when {
        storagePermissionState.status.isGranted -> {
            // If permission is granted, show the image picker card
            ImagePickerCard()
        }
        storagePermissionState.status.shouldShowRationale -> {
            // Show rationale if permission is not granted and a rationale should be displayed
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Storage permission is needed to select an image.")
                Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }

    }
//}
    //   if(openCamera){
    when {
        cameraPermissionState.status.isGranted -> {
            CameraScreen()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                Text("Camera Permission is not granted. Grant Permission to access Camera.")
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
    }
    // }
}


@Composable
fun ImagePickerCard(
    viewModel: AddProductViewModel = hiltViewModel()
) {
    // State for selected image URI
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    // Create a launcher to open the image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> viewModel.onImageSelected(uri) }
    )

    // Card to select or edit the image
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { launcher.launch("image/*") }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedImageUri != null) {
                // Display selected image
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Edit button in the top-right corner
                IconButton(
                    onClick = { launcher.launch("image/*") },
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
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add Image",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text("Tap to select an image", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun CameraWithPermissions() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        // Camera permission granted
        CameraScreen()
    } else {
        // Request camera permission
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }


    when {
        cameraPermissionState.status.isGranted -> {
            CameraScreen()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                Text("Camera Permission is not granted. Grant Permission to access Camera.")
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

@Composable
fun CameraScreen(modifier: Modifier = Modifier,
                 viewModel: AddProductViewModel = hiltViewModel()
) {

    val  context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val bitmaps by viewModel.bitmaps.collectAsState()

    Box(modifier = modifier
        .padding()
    ){
        if (bitmaps != null){
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {

                        // Display selected image
                        Image(
                            bitmap = bitmaps!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop

                        )
                        IconButton(
                            onClick = { viewModel.clearBitmap() },
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
                })


        }
        else {
            CameraPreview(
                controller = controller,
                modifier = modifier.fillMaxSize()
            )
            IconButton(
                onClick = {
                    controller.cameraSelector = if
                                                        (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    }else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                },
                modifier = modifier.offset(16.dp,16.dp)

            ) { Icon(
                imageVector = Icons.Default.Cameraswitch,contentDescription = null
            ) }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            onPhotoTaken = viewModel::onTakePhoto,
                            context = context
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "take photo"
                    )
                }
            }
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: Context
){

    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)


                onPhotoTaken(image.toBitmap())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera","Couldn't take photo",exception)
            }
        }

    )
}