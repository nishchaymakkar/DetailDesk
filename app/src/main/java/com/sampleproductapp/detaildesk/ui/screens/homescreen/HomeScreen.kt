@file:OptIn(ExperimentalMaterial3Api::class)

package com.sampleproductapp.detaildesk.ui.screens.homescreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sampleproductapp.detaildesk.modal.data.Product
import com.sampleproductapp.detaildesk.ui.components.ErrorScreen
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigatePS: () -> Unit,
    addProduct: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    HomeScreenContent(modifier = modifier,
        navigatePS = { navigatePS()},
        addProduct = {addProduct() },
        uiState = viewModel.productUiState,
        retryAction = viewModel::getProductsFromApi
    )
}


@Composable
fun HomeScreenContent(modifier: Modifier = Modifier,
                      uiState: ProductUiState,
                      retryAction: () -> Unit,
                      navigatePS: () -> Unit,
                      addProduct: () -> Unit) {
    Scaffold(
//        topBar = {
//            TopAppBar(
//                colors = TopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.onSecondary,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
//                    actionIconContentColor = MaterialTheme.colorScheme.primary,
//                    scrolledContainerColor = MaterialTheme.colorScheme.onPrimary
//
//                ),
//                title = { },
//                actions = {
//                    IconButton(
//                        onClick = {navigatePS()}
//                    ) {
//                        Icon(
//                            imageVector = Icons.Outlined.Person,
//                            contentDescription = null
//                        )
//                    }
//                }
//            )
//        },
        content = { it ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
                content = {
                    IconButton(
                        onClick = {navigatePS()},
                        modifier = modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    }
                    when (uiState) {
                        is ProductUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ProductUiState.Success -> {
                            Log.d("HomeScreenContent", "Success: ${uiState.products}")
                            ProductList(products = uiState.products)
                        }
                        is ProductUiState.Error -> {
                            ErrorScreen(retryAction = retryAction, modifier = Modifier.fillMaxSize())
                        }


                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addProduct()}
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null
                )
            }
        }
    )

}

@Composable
fun ProductList(
    products: List<Product>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(products) {index, item ->
            ProductItem(product = item)
        }

    }
}

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier) {


val byteArray = product.productImage.toBitmap()

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(16 / 9f),
                content = {


                        Image(bitmap = byteArray!!.asImageBitmap(),
                            modifier = modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null)


                    Row(modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        if (product.productName != null) {
                            Text(
                                text = product.productName ?: "unknown",
                                modifier.align(Alignment.CenterVertically),
                                fontSize = 12.sp
                            )
                        }
                        if (product.createdAt != null) {
                            Text(
                                text = product.createdAt ?: "unkown",
                                modifier.align(Alignment.CenterVertically),
                            )

                        }
                    }
                })
    }
}
fun String.toByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun String.toBitmap(): Bitmap? {
    val byteArray = Base64.decode(this, Base64.DEFAULT)
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true // Check dimensions only
    }
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

    // Calculate inSampleSize for downsampling
    options.inSampleSize = calculateInSampleSize(options, 800, 800)
    options.inJustDecodeBounds = false // Decode the actual bitmap

    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
